package cn.sunline.saas.loan.product.model.dto

import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.LoanTermType
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
    val configuration: DTOLoanProductConfiguration,
    val interestFeature: DTOInterestFeatureAdd
)


data class DTOLoanProductConfiguration(
    val maxAmount: BigDecimal?,
    val minAmount: BigDecimal?,
    val maxTerm: LoanTermType?,
    val minTerm: LoanTermType?,
)