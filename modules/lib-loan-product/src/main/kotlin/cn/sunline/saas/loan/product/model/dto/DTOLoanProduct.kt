package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.loan.product.model.LoanProductType
import java.math.BigDecimal

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:05
 */
data class DTOLoanProduct(
    var id: String?,
    var identificationCode: String,
    var name: String,
    var version: String,
    var description: String,
    var loanProductType: LoanProductType,
    var loanPurpose: String,
    var businessUnit: String,
    var status: BankingProductStatus?,
    var amountConfiguration: DTOAmountLoanProductConfiguration,
    var termConfiguration: DTOTermLoanProductConfiguration,
    var interestFeature: DTOInterestFeature,
    var repaymentFeature: DTORepaymentFeature,
    var feeFeatures: MutableList<DTOFeeFeature>?,
    var loanUploadConfigureFeatures: List<Long>? = listOf(),
    var documentTemplateFeatures:List<Long>? = listOf()
)

data class DTOAmountLoanProductConfiguration(
    val id: String?,
    var maxValueRange: String,
    var minValueRange: String,
)

data class DTOTermLoanProductConfiguration(
    val id: String?,
    val maxValueRange: LoanTermType,
    val minValueRange: LoanTermType,
)

data class DTOInterestFeature(
    val id: String?,
    val productId: String?,
    val interestType: InterestType,
    val ratePlanId: String,
    val interest: DTOInterestFeatureModality,
    val overdueInterest: DTOOverdueInterestFeatureModality
)

data class DTOInterestFeatureModality(
    val id: String?,
    val baseYearDays: BaseYearDays,
    val adjustFrequency: String,
    val basicPoint: BigDecimal?
)

data class DTOOverdueInterestFeatureModality(
    val id: String?,
    var overdueInterestRatePercentage: String
)

data class DTORepaymentFeature(
    val id: String?,
    val paymentMethod: PaymentMethodType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType,
    val prepaymentFeatureModality: MutableList<DTOPrepaymentFeatureModality>,
    val graceDays: Int
)

data class DTOPrepaymentFeatureModality(
    val id: String?,
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: BigDecimal
)

data class DTOFeeFeature(
    val id: String?,
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: BigDecimal?,
    val feeRate: BigDecimal?,
    val feeDeductType: FeeDeductType
)


