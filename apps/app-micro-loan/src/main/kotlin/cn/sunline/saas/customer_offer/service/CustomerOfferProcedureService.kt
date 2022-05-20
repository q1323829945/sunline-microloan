package cn.sunline.saas.customer_offer.service

import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer_offer.service.dto.DTOProductUploadConfig
import cn.sunline.saas.rpc.pubsub.CustomerOfferPublish
import cn.sunline.saas.rpc.pubsub.dto.DTODetail
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationData
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.service.PDPAMicroService
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.rpc.invoke.CustomerOfferProcedureInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class CustomerOfferProcedureService(
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

        val dtoLoanProduct = objectMapper.convertValue<ProductView>(loanProduct)

        val key = this.pdpaSign(dtoCustomerOffer.customerOfferProcedure.customerId,dtoCustomerOffer.pdpa.pdpaTemplateId,signature.originalFilename!!,signature.inputStream)

        dtoCustomerOffer.pdpa.signature = key
        val customerOfferProcedure = customerOfferService.initiate(dtoCustomerOffer)

        return DTOCustomerOfferView(customerOfferProcedure,dtoLoanProduct)
    }

    fun retrieve(customerOfferId:Long,countryCode:String):DTOCustomerOfferLoanView{
        val dtoCustomerOfferLoanView = customerLoanApplyService.retrieve(customerOfferId)

        val customerOffer = customerOfferService.getOne(customerOfferId)
        customerOffer?.run {
            //add customer offer procedure
            val dtoCustomerOffer = objectMapper.readValue<DTOCustomerOfferData>(customerOffer.data!!)
            val dTOCustomerOfferProcedureView = objectMapper.convertValue<DTOCustomerOfferProcedureView>(dtoCustomerOffer.customerOfferProcedure)
            dTOCustomerOfferProcedureView.customerOfferId = customerOffer.id
            dTOCustomerOfferProcedureView.status = customerOffer.status
            dtoCustomerOfferLoanView.customerOfferProcedure = dTOCustomerOfferProcedureView

            //add product info
            val loanProduct = getProduct(dtoCustomerOffer.product.productId)
            val dtoLoanProduct = objectMapper.convertValue<DTOProductView>(loanProduct)
            dtoCustomerOfferLoanView.product = dtoLoanProduct

            //add pdpa info
            val pdpa = getPDPA(countryCode)
            dtoCustomerOfferLoanView.pdpa = objectMapper.convertValue<PDPAInformationView>(pdpa)
        }

        return dtoCustomerOfferLoanView
    }

    fun submit(customerOfferId: Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<CustomerLoanApplyService.DTOFile>){
        val result = customerLoanApplyService.submit(customerOfferId, dtoCustomerOfferLoanAdd, dtoFile)

        val customerOffer = customerOfferService.getOne(customerOfferId)
        val kyc = result.kyc
        val detail = result.detail
        if(kyc != null && customerOffer != null && detail != null){
            val dtoLoanApplicationData = DTOLoanApplicationData(
                customerOfferId.toString(),
                DTODetail(
                    customerOffer.customerId.toString(),
                    detail.name,
                    detail.registrationNo
                )
            )

            customerOfferPublish.initiateUnderwriting(dtoLoanApplicationData)
        }

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
}