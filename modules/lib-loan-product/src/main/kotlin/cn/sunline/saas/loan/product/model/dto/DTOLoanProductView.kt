package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.document.template.modules.db.LoanUploadConfigure
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.loan.product.model.LoanProductType
import java.math.BigDecimal

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 11:28
 */
data class DTOLoanProductView(
        val id:String,
        val identificationCode: String,
        val name: String,
        val version: String,
        val description: String,
        val loanProductType: LoanProductType,
        val loanPurpose: String,
        val status: BankingProductStatus,
        var amountConfiguration: DTOAmountLoanProductConfigurationView?,
        var termConfiguration: DTOTermLoanProductConfigurationView?,
        var interestFeature: DTOInterestFeatureView?,
        var repaymentFeature: DTORepaymentFeatureView?,
        var feeFeatures:MutableList<DTOFeeFeatureView>?,
        var loanUploadConfigureFeatures:MutableList<LoanUploadConfigureView>?
)
data class LoanUploadConfigureView(
        val id: String,
        val name: String,
)

data class DTOAmountLoanProductConfigurationView(
        val id: String,
        var maxValueRange: String?,
        var minValueRange: String?,
)

data class DTOTermLoanProductConfigurationView(
        val id: String,
        var maxValueRange: LoanTermType?,
        var minValueRange: LoanTermType?,
)

data class DTOInterestFeatureView(
        val id: String,
        val productId: String,
        val interestType: InterestType,
        val ratePlanId: Long,
        val interest: DTOInterestFeatureModalityView,
        val overdueInterest: DTOOverdueInterestFeatureModalityView
)

data class DTOInterestFeatureModalityView(
        val id: String,
        val baseYearDays: BaseYearDays,
        val adjustFrequency: String
)

data class DTOOverdueInterestFeatureModalityView(
        val id: String,
        var overdueInterestRatePercentage: String
)

data class DTORepaymentFeatureView(
        val id: String,
        val productId: String,
        var payment: DTORepaymentFeatureModalityView,
        var prepayment: MutableList<DTOPrepaymentFeatureModalityView>,
)
data class DTORepaymentFeatureModalityView(
        val id: String,
        var paymentMethod: PaymentMethodType,
        var frequency: RepaymentFrequency,
        var repaymentDayType: RepaymentDayType
)

data class DTOPrepaymentFeatureModalityView(
        val id: String?,
        val term: LoanTermType,
        val type: PrepaymentType,
        val penaltyRatio: BigDecimal
)

data class DTOFeeFeatureView(
        val id: String?,
        val feeType: LoanFeeType,
        val feeMethodType: FeeMethodType,
        val feeAmount: BigDecimal?,
        val feeRate: BigDecimal?,
        val feeDeductType: FeeDeductType
)


//data class DTOLoanProductView(
//        val id:String? = null,
//        val identificationCode: String? = null,
//        val name: String? = null,
//        val version: String? = null,
//        val description: String? = null,
//        val loanProductType: LoanProductType? = null,
//        val loanPurpose: String? = null,
//        val status: BankingProductStatus? = null,
//        var amountConfiguration: DTOAmountLoanProductConfigurationView? = null,
//        var termConfiguration: DTOTermLoanProductConfigurationView? = null,
//        var interestFeature: InterestFeature? = null,
//        var repaymentFeature: RepaymentFeature? = null,
//        var feeFeatures:MutableList<FeeFeature>? = null,
//        var loanUploadConfigureFeatures:MutableList<LoanUploadConfigure>? = null
//)
//
//data class DTOAmountLoanProductConfigurationView(
//        val id: String? = null,
//        var maxValueRange: String? = null,
//        var minValueRange: String? = null,
//)
//
//data class DTOTermLoanProductConfigurationView(
//        val id: String? = null,
//        var maxValueRange: LoanTermType? = null,
//        var minValueRange: LoanTermType? = null,
//)