package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:05
 */
data class DTOLoanProductAdd(
    val identificationCode: String,
    val name: String,
    var version: String?,
    val description: String,
    val loanProductType: LoanProductType,
    val loanPurpose: String,
    val amountConfiguration: DTOAmountLoanProductConfiguration?,
    val termConfiguration: DTOTermLoanProductConfiguration?,
    val interestFeature: DTOInterestFeatureAdd?,
    val repaymentFeature: DTORepaymentFeatureAdd?,
    val feeFeatures:MutableList<DTOFeeFeatureAdd>?,
    var loanUploadConfigureFeatures:List<Long>? = listOf()
)

data class DTOAmountLoanProductConfiguration(
    val maxValueRange: String?,
    val minValueRange: String?,
)

data class DTOTermLoanProductConfiguration(
    val maxValueRange: LoanTermType?,
    val minValueRange: LoanTermType?,
)


