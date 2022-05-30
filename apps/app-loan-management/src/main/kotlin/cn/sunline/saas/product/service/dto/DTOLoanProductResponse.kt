package cn.sunline.saas.product.service.dto

import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.loan.product.model.LoanProductType

data class DTOLoanProductResponse(
    val id: String,
    val loanProductType: LoanProductType,
    val loanPurpose: String,
    var businessUnit: String,
    var interestFeature: DTOInterestFeature,
    var repaymentFeature: DTORepaymentFeature,
    var feeFeatures: MutableList<DTOFeeFeature>?,
    var loanUploadConfigureFeatures: MutableList<DTOLoanUploadConfigure>?,
    var documentTemplateFeatures:MutableList<DocumentTemplate>?
)

data class DTOInterestFeature(
    val id: String,
    val interestType: InterestType,
    var ratePlan: MutableList<DTOInterestRate>?,
    val interest: DTOInterestModality,
    val overdueInterest: DTOOverdueInterest
)

data class DTOInterestRate(
    val id: String,
    val period: LoanTermType,
    val rate: String,
)

data class DTOInterestModality(
    val baseYearDays: BaseYearDays,
    val adjustFrequency: String
)

data class DTOOverdueInterest(
    val overdueInterestRatePercentage: String
)

data class DTORepaymentFeature(
    val id: String,
    val payment: DTORepaymentFeatureModality,
    val prepayment: MutableList<DTOPrepaymentFeatureModality>
)

data class DTORepaymentFeatureModality(
    val id: String,
    val paymentMethod: PaymentMethodType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType
)

data class DTOPrepaymentFeatureModality(
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: String
)

data class DTOFeeFeature(
    val id: String,
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: String?,
    val feeRate: String?,
    val feeDeductType: FeeDeductType
)

data class DTOLoanUploadConfigure(
    val id: String,
    val name: String,
)

data class DocumentTemplate(
    val id:String,
    val name:String,
    val documentStoreReference:String,
    val fileType: FileType,
    val languageType: LanguageType,
    val documentType: DocumentType
)