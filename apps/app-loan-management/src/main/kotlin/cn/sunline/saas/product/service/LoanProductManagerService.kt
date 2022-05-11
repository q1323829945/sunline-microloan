package cn.sunline.saas.product.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.product.exception.LoanProductBusinessException
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class LoanProductManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductService: LoanProductService

    fun getPaged(
        name: String?,
        loanProductType: LoanProductType?,
        loanPurpose: String?,
        pageable: Pageable
    ): Page<DTOLoanProductView> {
        val paged = loanProductService.getLoanProductPaged(name, loanProductType, loanPurpose, pageable)
        return paged.map { objectMapper.convertValue<DTOLoanProductView>(it) }
    }

    fun addOne(loanProductData: DTOLoanProduct): DTOLoanProduct {
        checkTermConditions(
            loanProductData.termConfiguration.maxValueRange.days,
            loanProductData.termConfiguration.minValueRange.days
        )
        checkAmountConditions(
            BigDecimal(loanProductData.amountConfiguration.maxValueRange),
            BigDecimal(loanProductData.amountConfiguration.maxValueRange)
        )
        val oldLoanProduct =
            loanProductService.findByIdentificationCode(loanProductData.identificationCode).maxByOrNull {
                it.version
            }

        if (oldLoanProduct != null && oldLoanProduct.status == BankingProductStatus.INITIATED) {
            throw LoanProductBusinessException(
                "The version of loan's product has already exist",
                ManagementExceptionCode.DATA_ALREADY_EXIST
            )
        }

        loanProductData.repaymentFeature?.prepaymentFeatureModality?.groupBy { it.term }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the type of prepayment's term was already exist,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        loanProductData.feeFeatures?.groupBy { it.feeType }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the feeType of feeFeatures was more than once,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        loanProductData.version = if (oldLoanProduct == null) "1" else (oldLoanProduct.version.toInt() + 1).toString()
        return loanProductService.register(loanProductData)
    }

    fun getOne(id: Long): DTOLoanProduct {
        return loanProductService.getLoanProduct(id)
    }

    fun updateOne(
        id: Long,
        dtoLoanProduct: DTOLoanProduct
    ): DTOLoanProduct {
        checkProductStatus(id, false)
        checkTermConditions(
            dtoLoanProduct.termConfiguration.maxValueRange.days,
            dtoLoanProduct.termConfiguration.minValueRange.days
        )
        checkAmountConditions(
            BigDecimal(dtoLoanProduct.amountConfiguration.maxValueRange),
            BigDecimal(dtoLoanProduct.amountConfiguration.maxValueRange)
        )

        dtoLoanProduct.repaymentFeature?.prepaymentFeatureModality?.groupBy { it.term }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the type of prepayment's term was already exist,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        dtoLoanProduct.feeFeatures?.groupBy { it.feeType }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the feeType of feeFeatures was more than once,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        return loanProductService.updateLoanProduct(id, dtoLoanProduct)
    }

    fun updateStatus(
        id: Long,
        dtoLoanProductStatus: BankingProductStatus
    ): ResponseEntity<DTOResponseSuccess<LoanProduct>> {
        checkProductStatus(id, true)
        val view = loanProductService.updateLoanProductStatus(id, dtoLoanProductStatus)
        return DTOResponseSuccess(view).response()
    }

    fun getProductInfo(identificationCode: String): List<DTOLoanProduct> {
        return loanProductService.findByIdentificationCode(identificationCode)
    }

    fun getLoanProductHistoryList(identificationCode: String): List<DTOLoanProduct> {
        return loanProductService.findByIdentificationCodeAndStatus(identificationCode, BankingProductStatus.OBSOLETE)
    }

    fun getLoanProductListByStatus(
        bankingProductStatus: BankingProductStatus?,
        pageable: Pageable
    ): Page<LoanProduct> {
        val status = bankingProductStatus?.name ?: BankingProductStatus.SOLD.name
        return loanProductService.getLoanProductListByStatus(status, pageable)
    }

    private fun checkTermConditions(termMaxValueRange: Int, termMinValueRange: Int) {
        if (termMaxValueRange < termMinValueRange) {
            throw LoanProductBusinessException(
                "The term's config of product was error",
                ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
            )
        }
    }

    private fun checkAmountConditions(amountMaxValueRange: BigDecimal, amountMinValueRange: BigDecimal) {
        if (amountMaxValueRange < amountMinValueRange) {
            throw LoanProductBusinessException(
                "The amount's config of product was error",
                ManagementExceptionCode.PRODUCT_AMOUNT_CONFIG_MAX_MIN_ERROR
            )
        }
    }

    private fun checkProductStatus(id: Long, isUpdateStatus: Boolean) {
        val loanProduct = loanProductService.getLoanProduct(id)
        val status = loanProduct.status
        if (isUpdateStatus && BankingProductStatus.OBSOLETE == status) {
            throw LoanProductBusinessException(
                "The status of product was OBSOLETE,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
        if (isUpdateStatus && BankingProductStatus.SOLD == status) {
            throw LoanProductBusinessException(
                "The status of product was SOLD,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
        if (!isUpdateStatus && BankingProductStatus.INITIATED != status) {
            throw LoanProductBusinessException(
                "The status of product was not INITIATED,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
    }
}