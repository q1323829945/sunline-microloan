package cn.sunline.saas.controllers

import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService.DTOFile
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureView
import cn.sunline.saas.loan.configure.services.LoanUploadConfigureService
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.pdpa.services.PDPAService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Pageable

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("CustomerOffer")
class CustomerOfferProcedureController {
    @Autowired
    private lateinit var customerLoanApplyService: CustomerLoanApplyService

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var loanProductService: LoanProductService

    @Autowired
    private lateinit var pdpaService: PDPAService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping(value = ["loan/initiate"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun recordLoanApply(@RequestPart("customerOffer") dtoCustomerOffer: DTOCustomerOfferAdd, @RequestPart("signature") signature: MultipartFile):ResponseEntity<DTOResponseSuccess<DTOCustomerOfferView>> {
        //get product info
        val loanProduct = loanProductService.findById(dtoCustomerOffer.product.productId)

        val dtoLoanProduct = objectMapper.convertValue<ProductView>(loanProduct)
        dtoLoanProduct.productId = loanProduct.id

        val key = pdpaService.sign(dtoCustomerOffer.customerOfferProcedure.customerId,dtoCustomerOffer.pdpa.pdpaTemplateId,signature.originalFilename!!,signature.inputStream)

        dtoCustomerOffer.pdpa.signature = key
        val customerOfferProcedure = customerOfferService.initiate(dtoCustomerOffer)

        return DTOResponseSuccess(DTOCustomerOfferView(customerOfferProcedure,dtoLoanProduct)).response()
    }

    @PutMapping(value = ["loan/{customerOfferId}/submit"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun submitLoanApply(@PathVariable customerOfferId:Long, @RequestPart("customer")dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, @RequestParam("files")files: List<MultipartFile>){
        val fileList = ArrayList<DTOFile>()
        files.forEach {
            fileList.add(DTOFile(it.originalFilename!!, it.inputStream))
        }
        customerLoanApplyService.submit(customerOfferId, dtoCustomerOfferLoanAdd,fileList)
    }

    @PutMapping(value = ["loan/{customerOfferId}/update"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateLoanApply(@PathVariable customerOfferId:Long, @RequestPart("customer")dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, @RequestParam("files")files: List<MultipartFile>){
        val fileList = ArrayList<DTOFile>()
        files.forEach {
            fileList.add(DTOFile(it.originalFilename!!, it.inputStream))
        }
        customerLoanApplyService.update(customerOfferId, dtoCustomerOfferLoanAdd,fileList)
    }


    @GetMapping("loan/{customerOfferId}/{countryCode}/retrieve")
    fun retrieveLoanApply(@PathVariable("customerOfferId")customerOfferId:Long,@PathVariable("countryCode")countryCode:String):ResponseEntity<DTOResponseSuccess<DTOCustomerOfferLoanView>>{
        val dtoCustomerOfferLoanView = customerLoanApplyService.retrieve(customerOfferId, countryCode)
        val loanProduct = loanProductService.findById(dtoCustomerOfferLoanView.product.productId)

        val dtoLoanProduct = objectMapper.convertValue<DTOProductView>(loanProduct)
        dtoLoanProduct.productId = loanProduct.id

        dtoCustomerOfferLoanView.product = dtoLoanProduct

        return DTOResponseSuccess(dtoCustomerOfferLoanView).response()
    }

    @GetMapping("loan/{customerId}/list")
    fun getPaged(@PathVariable("customerId")customerId:Long,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val page = customerOfferService.getCustomerOfferPaged(customerId, pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("loanUploadTemplate")
    fun getLoanUploadTemplate():ResponseEntity<DTOResponseSuccess<List<DTOUploadConfigureView>>>{
        val list = loanUploadConfigureService.findAll()
        val responseEntity = objectMapper.convertValue<List<DTOUploadConfigureView>>(list)

        return DTOResponseSuccess(responseEntity).response()
    }
}