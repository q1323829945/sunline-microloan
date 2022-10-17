package cn.sunline.saas.product.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementView
import cn.sunline.saas.fee.arrangement.service.FeeArrangementService
import cn.sunline.saas.fee.arrangement.service.FeeItemService
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.TermType
import cn.sunline.saas.interest.arrangement.exception.BaseRateNullException
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.controller.dto.DTORatePlanWithInterestRates
import cn.sunline.saas.interest.exception.InterestRateBusinessException
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.product.exception.LoanProductBusinessException
import cn.sunline.saas.product.exception.LoanProductNotFoundException
import cn.sunline.saas.product.service.dto.DTOLoanProductResponse
import cn.sunline.saas.product.service.dto.DTOLoanUploadConfigure
import cn.sunline.saas.repayment.arrangement.service.RepaymentAccountService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.ProductInvoke
import cn.sunline.saas.schedule.ScheduleService
import cn.sunline.saas.schedule.util.ScheduleHelper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.math.BigDecimal


@Service
class LoanProductManagerService(
    val productInvoke: ProductInvoke,
    private val tenantDateTime: TenantDateTime
) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductService: LoanProductService

    @Autowired
    private lateinit var feeArrangementService: FeeArrangementService

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    @Autowired
    private lateinit var feeItemService: FeeItemService

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
            loanProductData.termConfiguration.minValueRange,
            loanProductData.termConfiguration.maxValueRange,
            loanProductData.amountConfiguration.minValueRange.toBigDecimal(),
            loanProductData.amountConfiguration.maxValueRange.toBigDecimal()
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
            updateStatus(this.id.toLong(), BankingProductStatus.OBSOLETE)
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
            dtoLoanProduct.termConfiguration.minValueRange,
            dtoLoanProduct.termConfiguration.maxValueRange,
            dtoLoanProduct.amountConfiguration.minValueRange.toBigDecimal(),
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
        fromPeriod: LoanTermType,
        toPeriod: LoanTermType,
        fromAmount: BigDecimal,
        toAmount: BigDecimal
    ) {
        if (toPeriod.term.toMonthUnit().num < fromPeriod.term.toMonthUnit().num) {
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
        val ratePlan = productInvoke.getRatePlanWithInterestRate(product.interestFeature!!.ratePlanId.toLong())
        val loanProduct = objectMapper.convertValue<DTOLoanProductResponse>(product)
        loanProduct.interestFeature.ratePlan = objectMapper.convertValue(ratePlan.rates)
        loanProduct.interestFeature.ratePlanType = ratePlan.type
//        loanProduct.interestFeature.ratePlanId = ratePlan.id
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
                if (minValueRange != null && maxValueRange != null && it in minValueRange..maxValueRange) {
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

    fun calculateTrailSchedule(
        amount: String,
        term: LoanTermType,
        dtoLoanProduct: DTOLoanProduct
    ): ScheduleHelper.DTORepaymentScheduleTrialView {

        val feeArrangement = dtoLoanProduct.feeFeatures?.run {
            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
                this
            )
        }

        val feeDeductItem = feeArrangementService.getDisbursementFeeDeductItem(feeArrangement, amount.toBigDecimal())
        val baseRateResult = ratePlanService.findByType(RatePlanType.STANDARD) ?:throw LoanProductBusinessException(
            "invalid rate plan",
            ManagementExceptionCode.RATE_PLAN_NOT_FOUND
        )
        val baseRateModel = baseRateResult.rates.map { objectMapper.convertValue<InterestRate>(it) }.toMutableList()
        val rateResult = ratePlanService.getOne(dtoLoanProduct.interestFeature.ratePlanId.toLong())?:throw LoanProductBusinessException(
            "invalid rate plan",
            ManagementExceptionCode.RATE_PLAN_NOT_FOUND
        )
        val ratesModel = rateResult.rates.map { objectMapper.convertValue<InterestRate>(it) }.toMutableList()
        val interestRate =
            InterestRateHelper.getExecutionRate(
                dtoLoanProduct.interestFeature.interestType,
                term,
                amount.toBigDecimal(),
                dtoLoanProduct.interestFeature.interest.basicPoint,
                baseRateModel,
                ratesModel
            )
        val schedule = ScheduleService(
            amount.toBigDecimal(),
            interestRate,
            term,
            dtoLoanProduct.repaymentFeature.frequency,
            dtoLoanProduct.repaymentFeature.repaymentDayType,
            dtoLoanProduct.interestFeature.interest.baseYearDays,
            tenantDateTime.now(),
            null,
            null,
            feeDeductItem.scheduleFee
        ).getSchedules(dtoLoanProduct.repaymentFeature.paymentMethod)
        return ScheduleHelper.convertToScheduleTrialMapper(schedule, feeDeductItem.immediateFee)
    }
}
