package cn.sunline.saas.product.controller

import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductChange
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
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


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    data class DTOLoanProductStatus(
            val status: BankingProductStatus
    )


    @Autowired
    private lateinit var loanProductService: LoanProductService

    @GetMapping
    fun getPaged(@RequestParam(required = false)name:String? = null,
                 @RequestParam(required = false)loanProductType: LoanProductType? = null,
                 @RequestParam(required = false)loanPurpose: String? = null,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {


        val paged = loanProductService.getLoanProductPaged(name,loanProductType,loanPurpose,pageable)

        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTOLoanProductView>(it)}).response()
    }

    @PostMapping
    fun addOne(@RequestBody loanProductData: DTOLoanProductAdd): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val view = loanProductService.register(loanProductData)
        return DTOResponseSuccess(view).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val view = loanProductService.getLoanProduct(id)
        return DTOResponseSuccess(view).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoLoanProduct: DTOLoanProductChange): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val view = loanProductService.updateLoanProduct(id,dtoLoanProduct)
        return DTOResponseSuccess(view).response()

    }

    @PutMapping("status/{id}")
    fun updateStatus(@PathVariable id: Long, @RequestBody dtoLoanProduct: DTOLoanProductStatus): ResponseEntity<DTOResponseSuccess<LoanProduct>> {

        val loanProduct = loanProductService.updateLoanProductStatus(id,dtoLoanProduct.status)

        return DTOResponseSuccess(loanProduct).response()

    }



}