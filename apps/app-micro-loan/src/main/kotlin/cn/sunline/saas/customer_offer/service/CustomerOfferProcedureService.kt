package cn.sunline.saas.customer_offer.service

import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.service.PDPAMicroService
import cn.sunline.saas.product.service.ProductService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class CustomerOfferProcedureService {
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

        val customerOffer = customerOfferService.getOneById(customerOfferId)
        customerOffer?.run {
            //add customer offer procedure
            val dtoCustomerOffer = objectMapper.readValue(customerOffer.data,DTOCustomerOfferAdd::class.java)
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

    private fun getProduct(productId:Long): DTOLoanProduct {
        return productService.findById(productId)
    }


    private fun pdpaSign(customerId:Long,pdpaTemplateId:Long,originalFilename:String,inputStream: InputStream):String{
        return pdpaMicroService.sign(customerId,pdpaTemplateId,originalFilename,inputStream)
    }

    private fun getPDPA(countryCode:String): PDPAInformation {
        return pdpaMicroService.retrieve(countryCode)
    }
}