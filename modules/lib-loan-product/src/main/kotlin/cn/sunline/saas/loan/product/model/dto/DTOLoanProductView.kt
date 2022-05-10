package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.db.InterestFeature
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.repayment.model.db.RepaymentFeature

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
        var interestFeature: InterestFeature?,
        var repaymentFeature: RepaymentFeature?,
        var feeFeatures:MutableList<FeeFeature>?,
        var loanUploadConfigureFeatures:MutableList<LoanUploadConfigure>?
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