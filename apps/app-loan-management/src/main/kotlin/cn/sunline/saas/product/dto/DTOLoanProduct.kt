package cn.sunline.saas.product.dto

import cn.sunline.saas.loan.product.model.dto.DTOAmountLoanProductConfiguration
import cn.sunline.saas.loan.product.model.dto.DTOTermLoanProductConfiguration

data class DTOLoanProduct(
    var productId:Long,
    val identificationCode:String,
    val name:String,
    val version:String? = null,
    val description:String,
    val amountConfiguration: DTOAmountLoanProductConfiguration?,
    val termConfiguration: DTOTermLoanProductConfiguration?,
)
