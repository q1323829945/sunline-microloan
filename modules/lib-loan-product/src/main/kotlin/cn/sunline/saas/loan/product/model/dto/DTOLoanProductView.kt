package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.db.InterestFeature
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.repayment.model.db.RepaymentFeature
import java.math.BigDecimal

/**
 * @title: DTOLoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 11:28
 */
data class DTOLoanProductView(
        val id:Long,
        val identificationCode: String,
        val name: String,
        val version: String,
        val description: String,
        val loanProductType: LoanProductType,
        val loanPurpose: String,
        var amountConfiguration: DTOAmountLoanProductConfigurationView?,
        var termConfiguration: DTOTermLoanProductConfigurationView?,
        var interestFeature: InterestFeature?,
        var repaymentFeature: RepaymentFeature?,
        var feeFeatures:MutableList<FeeFeature>?
)

data class DTOAmountLoanProductConfigurationView(
        val id:Long,
        val maxValueRange: BigDecimal?,
        val minValueRange: BigDecimal?,
)

data class DTOTermLoanProductConfigurationView(
        val id:Long,
        val maxValueRange: LoanTermType?,
        val minValueRange: LoanTermType?,
)