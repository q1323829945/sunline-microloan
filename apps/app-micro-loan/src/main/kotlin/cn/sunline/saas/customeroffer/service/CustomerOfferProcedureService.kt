package cn.sunline.saas.customeroffer.service

import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.service.PDPAService
import cn.sunline.saas.product.service.ProductService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class CustomerOfferProcedureService {
    @Autowired
    private lateinit var pdpaService: PDPAService

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

        val customerOffer = customerOfferService.getOneById(customerOfferId)
        customerOffer?.run {
            //add customer offer procedure
            val dtoCustomerOffer = objectMapper.treeToValue<DTOCustomerOfferAdd>(objectMapper.readTree(customerOffer.data))


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

    fun getCustomerOfferPaged(customerId:Long,pageable: Pageable):Page<DTOCustomerOfferPage>{
        val page = customerOfferService.getCustomerOfferPaged(customerId, pageable)
        page.forEach {
            val apply = customerLoanApplyService.getOne(it.customerOfferId)
            it.amount = apply?.amount.toString()
        }
        return page
    }

    private fun getProduct(productId:Long): DTOProductView{
        return productService.findById(productId)
    }


    private fun pdpaSign(customerId:Long,pdpaTemplateId:Long,originalFilename:String,inputStream: InputStream):String{
        return pdpaService.sign(customerId,pdpaTemplateId,originalFilename,inputStream)
    }

    private fun getPDPA(countryCode:String): PDPAInformation {
        return pdpaService.retrieve(countryCode)!!
    }
}