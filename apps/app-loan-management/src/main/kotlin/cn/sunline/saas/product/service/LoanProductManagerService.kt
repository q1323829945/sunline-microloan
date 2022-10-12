package cn.sunline.saas.product.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.TermType
import cn.sunline.saas.interest.exception.InterestRateBusinessException
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.product.exception.LoanProductBusinessException
import cn.sunline.saas.product.exception.LoanProductNotFoundException
import cn.sunline.saas.product.service.dto.DTOLoanProductResponse
import cn.sunline.saas.product.service.dto.DTOLoanUploadConfigure
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.ProductInvoke
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
class LoanProductManagerService(
    val productInvoke: ProductInvoke
) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductService: LoanProductService

    @Autowired
    private lateinit var interestRateService: InterestRateService

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    fun getPaged(
        name: String?,
        loanProductType: LoanProductType?,
        loanPurpose: String?,
        status: BankingProductStatus?,
        pageable: Pageable
    ): Page<DTOLoanProductView> {
        return loanProductService.getLoanProductPaged(name, loanProductType, loanPurpose, status, pageable).map {
            val product = objectMapper.convertValue<DTOLoanProductView>(it)
            loanProductService.setConfigurationOptions(it, product)
            product
        }
    }

    fun addOne(loanProductData: DTOLoanProduct): DTOLoanProductView {

        checkTermOrAmountConditions(
            loanProductData.interestFeature.ratePlanId.toLong(),
            loanProductData.termConfiguration.maxValueRange,
            loanProductData.termConfiguration.minValueRange,
            loanProductData.amountConfiguration.maxValueRange.toBigDecimal(),
            loanProductData.amountConfiguration.maxValueRange.toBigDecimal()
        )
        val oldLoanProduct =
            loanProductService.getPaged(
                null,
                null,
                null,
                loanProductData.identificationCode,
                null,
                true,
                Pageable.unpaged()
            ).content.maxByOrNull {
                it.version
            }

        if (oldLoanProduct != null && oldLoanProduct.status == BankingProductStatus.INITIATED) {
            throw LoanProductBusinessException(
                "The version of loan's product has already exist",
                ManagementExceptionCode.DATA_ALREADY_EXIST
            )
        }

        loanProductData.repaymentFeature.prepaymentFeatureModality.groupBy { it.term }.map {
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the type of prepayment's term was already exist,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        loanProductData.feeFeatures?.groupBy { it.feeType }?.map {
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the feeType of feeFeatures was more than once,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        loanProductData.version = oldLoanProduct?.run {
            updateStatus(this.id, BankingProductStatus.OBSOLETE)
            (this.version.toInt() + 1).toString()
        } ?: "1"

        return addBusinessUnitType(loanProductService.register(loanProductData))
    }

    fun getOne(id: Long): DTOLoanProductView {
        return addBusinessUnitType(loanProductService.getLoanProduct(id))
    }

    fun updateOne(
        id: Long,
        dtoLoanProduct: DTOLoanProduct
    ): DTOLoanProductView {

        checkProductStatus(id, false)
        checkTermOrAmountConditions(
            dtoLoanProduct.interestFeature.ratePlanId.toLong(),
            dtoLoanProduct.termConfiguration.maxValueRange,
            dtoLoanProduct.termConfiguration.minValueRange,
            dtoLoanProduct.amountConfiguration.maxValueRange.toBigDecimal(),
            dtoLoanProduct.amountConfiguration.maxValueRange.toBigDecimal()
        )
        dtoLoanProduct.repaymentFeature.prepaymentFeatureModality.groupBy { it.term }.map {
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the type of prepayment's term was already exist,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }
        dtoLoanProduct.feeFeatures?.groupBy { it.feeType }?.map {
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the feeType of feeFeatures was more than once,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }
        return addBusinessUnitType(loanProductService.updateLoanProduct(id, dtoLoanProduct))
    }

    fun updateStatus(
        id: Long,
        dtoLoanProductStatus: BankingProductStatus
    ): ResponseEntity<DTOResponseSuccess<LoanProduct>> {
        checkProductStatus(id, true)
        val view = loanProductService.updateLoanProductStatus(id, dtoLoanProductStatus)
        return DTOResponseSuccess(view).response()
    }

    fun getProductInfoList(identificationCode: String): List<DTOLoanProductView> {
        return loanProductService.findByIdentificationCode(identificationCode)
    }

    fun getLoanProductHistoryList(identificationCode: String): List<DTOLoanProductView> {
        return loanProductService.findByIdentificationCodeAndStatus(identificationCode, BankingProductStatus.OBSOLETE)
    }

    fun getLoanProductListByStatus(
        bankingProductStatus: BankingProductStatus?,
        pageable: Pageable
    ): Page<LoanProduct> {
        val status = bankingProductStatus ?: BankingProductStatus.SOLD
        return loanProductService.getLoanProductListByStatus(status, pageable)
    }

    private fun checkTermOrAmountConditions(
        ratePlanId: Long,
        fromPeriod: LoanTermType,
        toPeriod: LoanTermType,
        fromAmount: BigDecimal,
        toAmount: BigDecimal
    ) {
        if (toPeriod.term < fromPeriod.term) {
            throw LoanProductBusinessException(
                "the maximum term is less than the minimum term",
                ManagementExceptionCode.PRODUCT_TERM_VALUE_IS_NOT_CORRECT
            )
        }
        if (toAmount < fromAmount) {
            throw LoanProductBusinessException(
                "the maximum amount is less than the minimum amount",
                ManagementExceptionCode.PRODUCT_AMOUNT_VALUE_IS_NOT_CORRECT
            )
        }
        val ratePlan = ratePlanService.getOne(ratePlanId) ?: throw LoanProductBusinessException(
            "invalid rate plan",
            ManagementExceptionCode.RATE_PLAN_NOT_FOUND
        )
        val rates = interestRateService.findByRatePlanId(ratePlanId)

        when (ratePlan.type) {
            RatePlanType.CUSTOMER,
            RatePlanType.STANDARD -> {
                rates.map { it.fromPeriod }.let {
                    if (!it.contains(fromPeriod) || !it.contains(toPeriod)) {
                        throw LoanProductBusinessException(
                            "the selected interest rate plan does not contain the entered term value",
                            ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
                        )
                    }
                }
            }

            RatePlanType.LOAN_TERM_TIER_CUSTOMER -> {
                rates.map { it.fromPeriod }.let {
                    if (!it.contains(fromPeriod)) {
                        throw LoanProductBusinessException(
                            "the selected interest rate plan does not contain the minimum entered term value",
                            ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
                        )
                    }
                }
                rates.map { it.toPeriod }.let {
                    if (!it.contains(toPeriod)) {
                        throw LoanProductBusinessException(
                            "the selected interest rate plan does not contain the maximum entered term value",
                            ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
                        )
                    }
                }
            }

            RatePlanType.LOAN_AMOUNT_TIER_CUSTOMER -> {
                val minOf = rates.minOf { it.fromAmountPeriod!!.amount }
                if (fromAmount < minOf) {
                    throw LoanProductBusinessException(
                        "the minimum entered amount value is less than the minimum amount of the selected interest rate plan",
                        ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
                    )
                }
                val maxOf = rates.maxOf { it.toAmountPeriod!!.amount }
                if (toAmount > maxOf) {
                    throw LoanProductBusinessException(
                        "the maximum entered amount value is greater than the maximum amount of the selected interest rate plan",
                        ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
                    )
                }
            }
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
//        if (isUpdateStatus && BankingProductStatus.SOLD == status) {
//            throw LoanProductBusinessException(
//                "The status of product was SOLD,non-supported update",
//                ManagementExceptionCode.PRODUCT_STATUS_ERROR
//            )
//        }
        if (!isUpdateStatus && BankingProductStatus.INITIATED != status) {
            throw LoanProductBusinessException(
                "The status of product was not INITIATED,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
    }

    fun getInvokeOne(productId: Long): DTOLoanProductResponse {
        val product = loanProductService.getLoanProduct(productId)
        val list = productInvoke.getInterestRate(product.interestFeature!!.ratePlanId.toLong())
        val loanProduct = objectMapper.convertValue<DTOLoanProductResponse>(product)
        loanProduct.interestFeature.ratePlan = objectMapper.convertValue(list)
        return loanProduct
    }


    fun getUploadConfig(id: Long): List<DTOLoanUploadConfigure> {
        val product = loanProductService.getOne(id) ?: throw LoanProductNotFoundException("Invalid product")
        return product.loanUploadConfigureFeatures?.map {
            objectMapper.convertValue(it)
        } ?: listOf()
    }

    private fun addBusinessUnitType(product: DTOLoanProductView): DTOLoanProductView {
        val businessUnit = productInvoke.getBusinessUnit(product.businessUnit.toLong())
        product.business = objectMapper.convertValue(businessUnit)
        return product
    }


    fun getInterestRate(productId: Long): List<LoanTermType> {
        val interestFeature = loanProductService.getLoanProduct(productId)
        val interestRates = mutableListOf<LoanTermType>()
        interestFeature.termConfiguration?.run {
            val minValueRange = this.minValueRange
            val maxValueRange = this.maxValueRange
            LoanTermType.values().forEach {
                if (minValueRange != null && maxValueRange != null && it in minValueRange .. maxValueRange) {
                    interestRates.add(it)
                }
            }
        }
        return interestRates
    }


    fun getInvokeProducts(status: BankingProductStatus): List<DTOLoanProductView> {
        val products = this.getPaged(null, null, null, status, Pageable.unpaged()).content
        products.forEach { it.amountConfiguration }
        return products
    }
}
