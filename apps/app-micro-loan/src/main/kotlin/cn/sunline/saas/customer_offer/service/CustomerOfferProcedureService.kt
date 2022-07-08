package cn.sunline.saas.customer_offer.service

import cn.sunline.saas.customer.offer.modules.db.CustomerOffer
import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer.offer.exceptions.CustomerOfferNotFoundException
import cn.sunline.saas.customer_offer.service.dto.DTOCustomerOfferProcedure
import cn.sunline.saas.customer_offer.service.dto.DTOProductUploadConfig
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.rpc.pubsub.CustomerOfferPublish
import cn.sunline.saas.rpc.pubsub.dto.DTODetail
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationData
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.service.PDPAMicroService
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.rpc.invoke.CustomerOfferProcedureInvoke
import cn.sunline.saas.rpc.pubsub.dto.DTODocumentGeneration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class CustomerOfferProcedureService(
    private val tenantDateTime: TenantDateTime,
    private val customerOfferPublish: CustomerOfferPublish,
    private val customerOfferProcedureInvoke: CustomerOfferProcedureInvoke,
    private val obsApi: ObsApi) {
    @Autowired
    private lateinit var pdpaMicroService: PDPAMicroService

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var customerLoanApplyService: CustomerLoanApplyService

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun initiate(dtoCustomerOffer: DTOCustomerOfferAdd,signature: MultipartFile): DTOCustomerOfferView{
        //get product info
        val loanProduct = this.getProduct(dtoCustomerOffer.product.productId)

        val key = this.pdpaSign(dtoCustomerOffer.customerOfferProcedure.customerId,dtoCustomerOffer.pdpa.pdpaTemplateId,signature.originalFilename!!,signature.inputStream)
        dtoCustomerOffer.product.productName = loanProduct.name
        dtoCustomerOffer.pdpa.signature = key
        val customerOfferProcedure = customerOfferService.initiate(dtoCustomerOffer)

        //TODO: 登记时创建组织信息
        customerOfferPublish.registeredOrganisation()

        val dtoLoanProduct = objectMapper.convertValue<ProductView>(loanProduct)
        return DTOCustomerOfferView(customerOfferProcedure,dtoLoanProduct)
    }

    fun retrieve(customerOfferId:Long,countryCode:String):DTOCustomerOfferLoanView{
        val dtoCustomerOfferLoanView = customerLoanApplyService.retrieve(customerOfferId)

        val customerOffer = customerOfferService.getOne(customerOfferId)
        customerOffer?.run {
            //add customer offer procedure
            val dtoCustomerOffer = objectMapper.readValue<DTOCustomerOfferData>(customerOffer.data)
            dtoCustomerOfferLoanView.customerOfferProcedure = convertToDTOCustomerOfferProcedureView(this,dtoCustomerOffer)

            //add product info
            val loanProduct = getProduct(dtoCustomerOffer.product.productId)
            dtoCustomerOfferLoanView.product = objectMapper.convertValue<DTOProductView>(loanProduct)

            //add pdpa info
            val pdpa = getPDPA(countryCode)
            dtoCustomerOfferLoanView.pdpa = objectMapper.convertValue<PDPAInformationView>(pdpa)
        }

        return dtoCustomerOfferLoanView
    }

    private fun convertToDTOCustomerOfferProcedureView(
        customerOffer: CustomerOffer,
        dtoCustomerOfferData:DTOCustomerOfferData):DTOCustomerOfferProcedureView{
        val dTOCustomerOfferProcedureView = objectMapper.convertValue<DTOCustomerOfferProcedureView>(dtoCustomerOfferData.customerOfferProcedure)
        dTOCustomerOfferProcedureView.customerOfferId = customerOffer.id
        dTOCustomerOfferProcedureView.status = customerOffer.status
        return dTOCustomerOfferProcedureView
    }

    fun submit(customerOfferId: Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<CustomerLoanApplyService.DTOFile>){
        val result = customerLoanApplyService.submit(customerOfferId, dtoCustomerOfferLoanAdd, dtoFile)

        val customerOffer = customerOfferService.getOne(customerOfferId)?:throw CustomerOfferNotFoundException("Invalid customer offer")

        initiateUnderwriting(result, customerOffer)
        //TODO:文件生成,可能不是这一个步骤
        documentGeneration(result, customerOffer)

    }

    fun getProductUploadConfig(id:String):List<DTOProductUploadConfig>{
        val uploadConfigList =  customerOfferProcedureInvoke.getProductUploadConfig(id.toLong())
        return objectMapper.convertValue(uploadConfigList)
    }

    private fun getProduct(productId:Long): DTOLoanProductView {
        return productService.findById(productId)
    }


    private fun pdpaSign(customerId:Long,pdpaTemplateId:Long,originalFilename:String,inputStream: InputStream):String{
        val key = "$customerId/signature/$pdpaTemplateId/$originalFilename"
        obsApi.putObject(PutParams(key,inputStream))
        return key
    }

    private fun getPDPA(countryCode:String): PDPAInformation {
        return pdpaMicroService.retrieve(countryCode)
    }

    private fun initiateUnderwriting(result:DTOCustomerOfferLoanView,customerOffer: CustomerOffer){
        val dtoLoanApplicationData = DTOLoanApplicationData(
            customerOffer.id.toString(),
            DTODetail(
                customerOffer.customerId.toString(),
                result.detail!!.name,
                result.detail!!.registrationNo
            )
        )
        customerOfferPublish.initiateUnderwriting(dtoLoanApplicationData)
    }

    private fun documentGeneration(result:DTOCustomerOfferLoanView,customerOffer: CustomerOffer){
        val product = getProduct(customerOffer.productId)
        product.documentTemplateFeatures?.forEach {
            customerOfferPublish.documentGeneration(
                DTODocumentGeneration(
                    templateId = it.id.toLong(),
                    params = mapOf(),
                    generateType = FileType.PDF,
                    key = "${customerOffer.customerId}/${it.name}"
                )
            )
        }

    }

    fun getCustomerOfferPaged(customerId: Long,pageable: Pageable):Page<DTOCustomerOfferProcedure>{
        return customerOfferService.getCustomerOfferPaged(customerId,null,null,pageable).map {
            val loanApply = customerLoanApplyService.getOne(it.id!!)
            DTOCustomerOfferProcedure(
                customerOfferId = it.id.toString(),
                amount = loanApply?.run { this.amount?.toString() },
                datetime = tenantDateTime.toTenantDateTime(it.datetime).toString(),
                productName = it.productName,
                status = it.status
            )
        }
    }
}

