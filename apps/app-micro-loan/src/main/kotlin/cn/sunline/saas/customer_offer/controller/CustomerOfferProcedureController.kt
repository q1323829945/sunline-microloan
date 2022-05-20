package cn.sunline.saas.customer_offer.controller

import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer_offer.service.CustomerOfferProcedureService
import cn.sunline.saas.customer_offer.service.dto.DTOProductUploadConfig
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
    private lateinit var customerOfferProductService: CustomerOfferProcedureService

    @PostMapping(value = ["loan/initiate"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun recordLoanApply(@RequestPart("customerOffer") dtoCustomerOffer: DTOCustomerOfferAdd, @RequestPart("signature") signature: MultipartFile):ResponseEntity<DTOResponseSuccess<DTOCustomerOfferView>> {
        val dtoCustomerOfferView = customerOfferProductService.initiate(dtoCustomerOffer, signature)
        return DTOResponseSuccess(dtoCustomerOfferView).response()
    }

    @PutMapping(value = ["loan/{customerOfferId}/submit"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun submitLoanApply(@PathVariable customerOfferId:Long, @RequestPart("customer")dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, @RequestParam("files")files: List<MultipartFile>?){
        customerOfferProductService.submit(customerOfferId, dtoCustomerOfferLoanAdd,getDTOFileList(files))
    }

    @PutMapping(value = ["loan/{customerOfferId}/update"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateLoanApply(@PathVariable customerOfferId:Long, @RequestPart("customer")dtoCustomerOfferLoanChange: DTOCustomerOfferLoanChange, @RequestParam("files")files: List<MultipartFile>?){
        customerLoanApplyService.update(customerOfferId, dtoCustomerOfferLoanChange,getDTOFileList(files))
    }

    @GetMapping("loan/{customerOfferId}/{countryCode}/retrieve")
    fun retrieveLoanApply(@PathVariable("customerOfferId")customerOfferId:Long,@PathVariable("countryCode")countryCode:String):ResponseEntity<DTOResponseSuccess<DTOCustomerOfferLoanView>>{
        val dtoCustomerOfferLoanView = customerOfferProductService.retrieve(customerOfferId, countryCode)
        return DTOResponseSuccess(dtoCustomerOfferLoanView).response()
    }


    @GetMapping("loanUploadTemplate/{productId}")
    fun getLoanUploadTemplate(@PathVariable productId:String):ResponseEntity<DTOResponseSuccess<List<DTOProductUploadConfig>>>{
        return DTOResponseSuccess(customerOfferProductService.getProductUploadConfig(productId)).response()
    }

//    @GetMapping("loan/{customerId}/list")
//    fun getPaged(@PathVariable("customerId")customerId:Long,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
//        val page = customerOfferProductService.getCustomerOfferPaged(customerId, pageable)
//        return DTOPagedResponseSuccess(page.map { it }).response()
//    }

    private fun getDTOFileList(files: List<MultipartFile>?):List<CustomerLoanApplyService.DTOFile>{
        val fileList = ArrayList<CustomerLoanApplyService.DTOFile>()
        files?.forEach {
            fileList.add(CustomerLoanApplyService.DTOFile(it.originalFilename!!, it.inputStream))
        }

        return fileList
    }
}