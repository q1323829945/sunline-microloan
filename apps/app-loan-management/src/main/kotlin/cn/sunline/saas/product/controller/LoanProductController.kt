package cn.sunline.saas.product.controller

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.*
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.product.dto.DTOLoanProductStatus
import cn.sunline.saas.product.service.LoanProductManagerService
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
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


@RestController
@RequestMapping("/LoanProduct")
class LoanProductController {

    @Autowired
    private lateinit var loanProductManagerService: LoanProductManagerService

    @GetMapping
    fun getPaged(@RequestParam(required = false)name:String? = null,
                 @RequestParam(required = false)loanProductType: LoanProductType? = null,
                 @RequestParam(required = false)loanPurpose: String? = null,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        return loanProductManagerService.getPaged(name,loanProductType,loanPurpose,pageable)
    }

    @PostMapping
    fun addOne(@RequestBody loanProductData: DTOLoanProductAdd): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return loanProductManagerService.addOne(loanProductData)
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return loanProductManagerService.getOne(id)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoLoanProduct: DTOLoanProductChange): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return loanProductManagerService.updateOne(id,dtoLoanProduct)
    }

    @PutMapping("status/{id}")
    fun updateStatus(@PathVariable id: Long, @RequestBody dtoLoanProduct: DTOLoanProductStatus): ResponseEntity<DTOResponseSuccess<LoanProduct>> {
        return loanProductManagerService.updateStatus(id,dtoLoanProduct)
    }

    @GetMapping("{identificationCode}/retrieve")
    fun getProductInfo(@PathVariable identificationCode:String): ResponseEntity<DTOPagedResponseSuccess>{
        return loanProductManagerService.getProductInfo(identificationCode)
    }

    @GetMapping("all")
    fun getAll(): ResponseEntity<DTOPagedResponseSuccess> {
        return loanProductManagerService.getAll(pageable = Pageable.unpaged())
    }

}