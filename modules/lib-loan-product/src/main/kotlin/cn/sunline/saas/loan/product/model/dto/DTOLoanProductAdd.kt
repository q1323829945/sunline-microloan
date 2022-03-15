package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import java.math.BigDecimal

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:05
 */
data class DTOLoanProductAdd(
    val identificationCode: String,
    val name: String,
    val version: String,
    val description: String,
    val loanProductType: LoanProductType,
    val loanPurpose: String,
    val amountConfiguration: DTOAmountLoanProductConfiguration?,
    val termConfiguration: DTOTermLoanProductConfiguration?,
    val interestFeature: DTOInterestFeatureAdd?,
    val repaymentFeature: DTORepaymentFeatureAdd?,
    val feeFeatures:MutableList<DTOFeeFeatureAdd>?
)

data class DTOAmountLoanProductConfiguration(
    val maxValueRange: BigDecimal?,
    val minValueRange: BigDecimal?,
)

data class DTOTermLoanProductConfiguration(
    val maxValueRange: LoanTermType?,
    val minValueRange: LoanTermType?,
)


