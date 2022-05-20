package cn.sunline.saas.product.controller

import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct

import cn.sunline.saas.loan.product.model.dto.*import cn.sunline.saas.product.service.LoanProductManagerService
import cn.sunline.saas.product.service.dto.DTOLoanProductResponse
import cn.sunline.saas.product.service.dto.DTOLoanUploadConfigure
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/LoanProduct")
class LoanProductController {

    @Autowired
    private lateinit var loanProductManagerService: LoanProductManagerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(@RequestParam(required = false)name:String? = null,
                 @RequestParam(required = false)loanProductType: LoanProductType? = null,
                 @RequestParam(required = false)loanPurpose: String? = null,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {

        val paged = loanProductManagerService.getPaged(name,loanProductType,loanPurpose,pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PostMapping
    fun addOne(@RequestBody loanProductData: DTOLoanProduct): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val result = loanProductManagerService.addOne(loanProductData)
        return DTOResponseSuccess(result).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id: String): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val result = loanProductManagerService.getOne(id.toLong())
        return DTOResponseSuccess(result).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: String, @RequestBody dtoLoanProduct: DTOLoanProduct): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val result = loanProductManagerService.updateOne(id.toLong(),dtoLoanProduct)
        return DTOResponseSuccess(result).response()

    }

    @PutMapping("status/{id}")
    fun updateStatus(@PathVariable id: String, @RequestBody dtoLoanProductStatus: BankingProductStatus): ResponseEntity<DTOResponseSuccess<LoanProduct>> {
        return loanProductManagerService.updateStatus(id.toLong(),dtoLoanProductStatus)
    }

    @GetMapping("{identificationCode}/retrieve")
    fun getProductInfo(@PathVariable identificationCode:String): ResponseEntity<DTOPagedResponseSuccess>{
        val productList =  loanProductManagerService.getProductInfo(identificationCode)
        return DTOPagedResponseSuccess(productList.map { objectMapper.convertValue<DTOLoanProduct>(it) }).response()
    }

    @GetMapping("{identificationCode}/history")
    fun getLoanProductHistoryList(@PathVariable identificationCode:String): ResponseEntity<DTOPagedResponseSuccess>{
        val productList =  loanProductManagerService.getLoanProductHistoryList(identificationCode)
        return DTOPagedResponseSuccess(productList.map { objectMapper.convertValue<DTOLoanProduct>(it) }).response()
    }

    @GetMapping("allByStatus")
    fun getAllByStatus(): ResponseEntity<DTOPagedResponseSuccess> {
        val productList =   loanProductManagerService.getLoanProductListByStatus(BankingProductStatus.SOLD,Pageable.unpaged())
        return DTOPagedResponseSuccess(productList.map { objectMapper.convertValue<DTOLoanProduct>(it) }).response()
    }

    @GetMapping("invoke/{id}")
    fun getInvokeOne(@PathVariable id:String): DTOLoanProductResponse {
        return loanProductManagerService.getInvokeOne(id.toLong())
    }

    @GetMapping("uploadConfig/{id}")
    fun getUploadConfigById(@PathVariable id:String):List<DTOLoanUploadConfigure>{
        return loanProductManagerService.getUploadConfig(id.toLong())
    }

}