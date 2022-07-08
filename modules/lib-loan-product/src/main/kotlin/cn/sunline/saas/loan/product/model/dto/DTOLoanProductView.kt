package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType
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
        var businessUnit:String,
        var business:BusinessUnitView?,
        val status: BankingProductStatus,
        var amountConfiguration: DTOAmountLoanProductConfigurationView?,
        var termConfiguration: DTOTermLoanProductConfigurationView?,
        var interestFeature: DTOInterestFeatureView?,
        var repaymentFeature: DTORepaymentFeatureView?,
        var feeFeatures:MutableList<DTOFeeFeatureView>?,
        var loanUploadConfigureFeatures:MutableList<LoanUploadConfigureView>?,
        var documentTemplateFeatures:MutableList<DocumentTemplateView>?
)
data class BusinessUnitView(
    val id:String,
    val type:String
)


data class LoanUploadConfigureView(
        val id: String,
        val name: String,
)

data class DocumentTemplateView(
        val id:String,
        val name:String,
        val documentStoreReference:String,
        val fileType: FileType,
        val languageType: LanguageType,
        val documentType: DocumentType
)

data class DTOAmountLoanProductConfigurationView(
        val id: String,
        var maxValueRange: String,
        var minValueRange: String,
)

data class DTOTermLoanProductConfigurationView(
        val id: String,
        var maxValueRange: LoanTermType,
        var minValueRange: LoanTermType,
)

data class DTOInterestFeatureView(
        val id: String,
        val productId: String,
        val interestType: InterestType,
        val ratePlanId: String,
        val interest: DTOInterestFeatureModalityView,
        val overdueInterest: DTOOverdueInterestFeatureModalityView,
)

data class DTOInterestFeatureModalityView(
        val id: String,
        val baseYearDays: BaseYearDays,
        val adjustFrequency: String,
        val graceDays: Int
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
        var repaymentDayType: RepaymentDayType,
        val graceDays: Int
)

data class DTOPrepaymentFeatureModalityView(
        val id: String?,
        val term: LoanTermType,
        val type: PrepaymentType,
        val penaltyRatio: String
)

data class DTOFeeFeatureView(
        val id: String?,
        val feeType: LoanFeeType,
        val feeMethodType: FeeMethodType,
        val feeAmount: BigDecimal?,
        val feeRate: BigDecimal?,
        val feeDeductType: FeeDeductType
)

