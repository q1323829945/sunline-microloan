package cn.sunline.saas.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.ProductType
import java.math.BigDecimal

data class DTOLoanApplyAuditAdd(
    val applicationId:String,
    val name: String?,
    val productId: Long?,
    val term: LoanTermType?,
    val amount: BigDecimal?,
    val data:String,
    val status: ApplyStatus,
)
