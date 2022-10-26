package cn.sunline.saas.consumer_loan.invoke.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.loan.product.model.LoanProductType
import java.math.BigDecimal

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 16:57
 */
data class DTOLoanProduct(
    val id: String,
    val loanProductType: LoanProductType,
    val loanPurpose: String,
    val businessUnit: Long,
    var interestFeature: DTOInterestFeature,
    var repaymentFeature: DTORepaymentFeature,
    var feeFeatures: MutableList<DTOFeeFeature>?,
    var loanUploadConfigureFeatures: MutableList<DTOLoanUploadConfigure>?,
    var name: String
)

data class DTOInterestFeature(
    val id: Long,
    val interestType: InterestType,
    val ratePlan: MutableList<DTOInterestRate>,
    val ratePlanType: RatePlanType,
    val ratePlanId: Long,
    val interest: DTOInterestModality,
    val overdueInterest: DTOOverdueInterest
)

data class DTOInterestRate(
    val id: Long,
    val fromPeriod: LoanTermType? = null,
    val toPeriod: LoanTermType? = null,
    val fromAmountPeriod: BigDecimal? = null,
    val toAmountPeriod: BigDecimal? = null,
    val rate: String
)

data class DTOInterestModality(
    val baseYearDays: BaseYearDays,
    val adjustFrequency: String,
    val floatPoint: BigDecimal?,
    val floatRatio: BigDecimal?
)

data class DTOOverdueInterest(
    val overdueInterestRatePercentage: String
)

data class DTORepaymentFeature(
    val id: Long,
    val payment: DTORepaymentFeatureModality,
    val prepayment: MutableList<DTOPrepaymentFeatureModality>
)

data class DTORepaymentFeatureModality(
    val id: Long,
    val paymentMethod: PaymentMethodType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType,
    val graceDays: Int,
)

data class DTOPrepaymentFeatureModality(
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: String
)

data class DTOFeeFeature(
    val id: Long,
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: String?,
    val feeRate: String?,
    val feeDeductType: FeeDeductType
)

data class DTOLoanUploadConfigure(
    val id: Long,
    val name: String,
)
