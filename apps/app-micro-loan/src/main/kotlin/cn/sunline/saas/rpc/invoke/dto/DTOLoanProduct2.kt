package cn.sunline.saas.rpc.invoke.dto
//
//import cn.sunline.saas.fee.constant.FeeDeductType
//import cn.sunline.saas.fee.constant.FeeMethodType
//import cn.sunline.saas.global.constant.*
//import cn.sunline.saas.interest.constant.BaseYearDays
//import cn.sunline.saas.interest.constant.InterestType
//import cn.sunline.saas.loan.product.model.LoanProductType
//
///**
// * @title: DTOLoanProduct
// * @description: TODO
// * @author Kevin-Cui
// * @date 2022/5/5 16:57
// */
//data class DTOLoanProduct(
//    val id:String,
//    val identificationCode: String,
//    val name: String,
//    val version: String,
//    val description: String,
//    val loanProductType: LoanProductType,
//    val loanPurpose: String,
//    val status: BankingProductStatus,
//    val businessUnit:Long,
//    var amountConfiguration: DTOAmountLoanProductConfiguration?,
//    var termConfiguration: DTOTermLoanProductConfiguration?,
//    var interestFeature: DTOInterestFeature?,
//    var repaymentFeature: DTORepaymentFeature,
//    var feeFeatures:MutableList<DTOFeeFeature>?,
//    var loanUploadConfigureFeatures:MutableList<DTOLoanUploadConfigure>?
//)
//
//data class DTOAmountLoanProductConfiguration(
//    val id: String,
//    var maxValueRange: String?,
//    var minValueRange: String?,
//)
//
//data class DTOTermLoanProductConfiguration(
//    val id: String,
//    var maxValueRange: LoanTermType?,
//    var minValueRange: LoanTermType?,
//)
//
//data class DTOInterestFeature(
//    val id: Long,
//    val interestType: InterestType,
//    val ratePlanId: MutableList<DTOInterestRate>,
//    val interestModality : DTOInterestModality,
//    val overdueInterest: DTOOverdueInterest
//)
//
//data class DTOInterestRate(
//    val id:Long,
//    val period: LoanTermType,
//    val rate: String,
//)
//
//data class DTOInterestModality(
//    val baseYearDays: BaseYearDays,
//    val adjustFrequency: String
//)
//
//data class DTOOverdueInterest(
//    val overdueInterestRatePercentage: String
//)
//
//data class DTORepaymentFeature(
//    val id: Long,
//    val payment: DTORepaymentFeatureModality,
//    val prepayment:MutableList<DTOPrepaymentFeatureModality>
//)
//
//data class DTORepaymentFeatureModality(
//    val id: Long,
//    val paymentMethod: PaymentMethodType,
//    val frequency: RepaymentFrequency,
//    val repaymentDayType: RepaymentDayType
//)
//
//data class DTOPrepaymentFeatureModality(
//    val term: LoanTermType,
//    val type: PrepaymentType,
//    val penaltyRatio: String
//)
//
//data class DTOFeeFeature(
//    val id: Long,
//    val feeType: LoanFeeType,
//    val feeMethodType: FeeMethodType,
//    val feeAmount:String?,
//    val feeRate:String?,
//    val feeDeductType: FeeDeductType
//)
//
//data class DTOLoanUploadConfigure(
//    val id: Long,
//    val name: String,
//    val required: Boolean,
//    val directoryId: Long,
//)