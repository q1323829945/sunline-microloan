package cn.sunline.saas.product.service.impl

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductChange
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.product.dto.DTOBaseLoanProductView
import cn.sunline.saas.product.dto.DTOLoanProductStatus
import cn.sunline.saas.product.exception.LoanProductBusinessException
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
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class LoanProductManagerServiceImpl: LoanProductManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductService: LoanProductService

    override fun getPaged(name:String?,
                 loanProductType: LoanProductType?,
                 loanPurpose: String?,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = loanProductService.getLoanProductPaged(name,loanProductType,loanPurpose,pageable)
        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTOLoanProductView>(it)}).response()
    }

    override fun addOne(loanProductData: DTOLoanProductAdd): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        checkTermConditions(loanProductData.termConfiguration?.maxValueRange?.days!!,loanProductData.termConfiguration?.minValueRange?.days!!)
        checkAmountConditions(BigDecimal(loanProductData.amountConfiguration?.maxValueRange!!),BigDecimal(loanProductData.amountConfiguration?.maxValueRange!!))
        loanProductData.version = "1"
        val oldLoanProduct = loanProductService.findByIdentificationCode(loanProductData.identificationCode).maxByOrNull {
            it.version
        }
        if(oldLoanProduct != null && oldLoanProduct.status == BankingProductStatus.INITIATED){
            throw LoanProductBusinessException("The version of loan's product has already exist", ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        val view = loanProductService.register(loanProductData)
        return DTOResponseSuccess(view).response()
    }

    override fun getOne(id: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val view = loanProductService.getLoanProduct(id)
        return DTOResponseSuccess(view).response()
    }

    override fun updateOne(id: Long, dtoLoanProduct: DTOLoanProductChange): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        checkProductStatus(id)
        checkTermConditions(dtoLoanProduct.termConfiguration?.maxValueRange?.days!!,dtoLoanProduct.termConfiguration?.minValueRange?.days!!)
        checkAmountConditions(BigDecimal(dtoLoanProduct.amountConfiguration?.maxValueRange!!),BigDecimal(dtoLoanProduct.amountConfiguration?.maxValueRange!!))
        val view = loanProductService.updateLoanProduct(id,dtoLoanProduct)
        return DTOResponseSuccess(view).response()
    }

    override fun updateStatus(id: Long, dtoLoanProductStatus: DTOLoanProductStatus): ResponseEntity<DTOResponseSuccess<LoanProduct>> {
        checkProductStatus(id)
        val view = loanProductService.updateLoanProductStatus(id,dtoLoanProductStatus.status)
        return DTOResponseSuccess(view).response()
    }

    override fun getProductInfo(identificationCode:String): ResponseEntity<DTOPagedResponseSuccess> {
        val productList = loanProductService.findByIdentificationCode(identificationCode)
        return DTOPagedResponseSuccess(productList.map { objectMapper.convertValue<DTOLoanProductView>(it)}).response()
    }

    override fun getAll(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = loanProductService.getAllLoanProduct(pageable)
        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTOBaseLoanProductView>(it)}).response()
    }

    private fun checkTermConditions(termMaxValueRange: Int,termMinValueRange: Int){
        if(termMaxValueRange < termMinValueRange){
            throw LoanProductBusinessException("The term's config of product was error", ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR)
        }
    }

    private fun checkAmountConditions(amountMaxValueRange: BigDecimal,amountMinValueRange: BigDecimal){
        if(amountMaxValueRange < amountMinValueRange){
            throw LoanProductBusinessException("The amount's config of product was error", ManagementExceptionCode.PRODUCT_AMOUNT_CONFIG_MAX_MIN_ERROR)
        }
    }

    private fun checkProductStatus(id: Long){
        val loanProduct = loanProductService.getLoanProduct(id)
        val status = loanProduct.status
        if(BankingProductStatus.INITIATED != status){
            throw LoanProductBusinessException("The status of product was not INITIATED,non-supported update", ManagementExceptionCode.PRODUCT_STATUS_ERROR)
        }
    }
}