package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.fee.model.dto.DTOFeeFeatureChange
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureChange
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureChange

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:05
 */
data class DTOLoanProductChange(
    val identificationCode: String,
    val name: String,
    var version: String,
    val description: String,
    val loanProductType: LoanProductType,
    val loanPurpose: String,
    val status: BankingProductStatus,
    val amountConfiguration: DTOAmountLoanProductChangeConfiguration?,
    val termConfiguration: DTOTermLoanProductChangeConfiguration?,
    val interestFeature: DTOInterestFeatureChange?,
    val repaymentFeature: DTORepaymentFeatureChange?,
    val feeFeatures:MutableList<DTOFeeFeatureChange>?,
    var loanUploadConfigureFeatures:List<Long>? = listOf()
)

data class DTOAmountLoanProductChangeConfiguration(
    val id:Long?,
    val maxValueRange: String?,
    val minValueRange: String?,
)

data class DTOTermLoanProductChangeConfiguration(
    val id:Long?,
    val maxValueRange: LoanTermType?,
    val minValueRange: LoanTermType?,
)


