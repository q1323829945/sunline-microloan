package cn.sunline.saas.modules.dto

import java.math.BigDecimal

data class DTOCustomerLoanApplyAuditAdd(
    var customerOfferId:Long,
    var amount: BigDecimal?,
    var data:String,
)
