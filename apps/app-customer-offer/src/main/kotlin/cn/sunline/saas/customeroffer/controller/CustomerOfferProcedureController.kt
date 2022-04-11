package cn.sunline.saas.customeroffer.controller

import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customeroffer.service.CustomerOfferProcedureService
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureView
import cn.sunline.saas.loan.configure.services.LoanUploadConfigureService
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
    private lateinit var customerOfferProductService: CustomerOfferProcedureService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping(value = ["loan/initiate"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun recordLoanApply(@RequestPart("customerOffer") dtoCustomerOffer: DTOCustomerOfferAdd, @RequestPart("signature") signature: MultipartFile):ResponseEntity<DTOResponseSuccess<DTOCustomerOfferView>> {
        val dtoCustomerOfferView = customerOfferProductService.initiate(dtoCustomerOffer, signature)
        return DTOResponseSuccess(dtoCustomerOfferView).response()
    }

    @PutMapping(value = ["loan/{customerOfferId}/submit"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun submitLoanApply(@PathVariable customerOfferId:Long, @RequestPart("customer")dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, @RequestParam("files")files: List<MultipartFile>?){
        customerLoanApplyService.submit(customerOfferId, dtoCustomerOfferLoanAdd,getDTOFileList(files))
    }

    @PutMapping(value = ["loan/{customerOfferId}/update"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateLoanApply(@PathVariable customerOfferId:Long, @RequestPart("customer")dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, @RequestParam("files")files: List<MultipartFile>?){
        customerLoanApplyService.update(customerOfferId, dtoCustomerOfferLoanAdd,getDTOFileList(files))
    }

    @GetMapping("loan/{customerOfferId}/{countryCode}/retrieve")
    fun retrieveLoanApply(@PathVariable("customerOfferId")customerOfferId:Long,@PathVariable("countryCode")countryCode:String):ResponseEntity<DTOResponseSuccess<DTOCustomerOfferLoanView>>{
        val dtoCustomerOfferLoanView = customerOfferProductService.retrieve(customerOfferId, countryCode)
        return DTOResponseSuccess(dtoCustomerOfferLoanView).response()
    }

    @GetMapping("loan/{customerId}/list")
    fun getPaged(@PathVariable("customerId")customerId:Long,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val page = customerOfferService.getCustomerOfferPaged(customerId, pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("loanUploadTemplate")
    fun getLoanUploadTemplate(@RequestParam(required = true) productId: Long):ResponseEntity<DTOResponseSuccess<List<DTOUploadConfigureView>>>{
        val list = loanUploadConfigureService.findAllExist(productId)
        val responseEntity = objectMapper.convertValue<List<DTOUploadConfigureView>>(list)
        return DTOResponseSuccess(responseEntity).response()
    }

    private fun getDTOFileList(files: List<MultipartFile>?):List<CustomerLoanApplyService.DTOFile>{
        val fileList = ArrayList<CustomerLoanApplyService.DTOFile>()
        files?.forEach {
            fileList.add(CustomerLoanApplyService.DTOFile(it.originalFilename!!, it.inputStream))
        }

        return fileList
    }
}