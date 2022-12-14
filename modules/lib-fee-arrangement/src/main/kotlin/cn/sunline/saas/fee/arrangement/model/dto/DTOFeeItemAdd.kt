package cn.sunline.saas.fee.arrangement.model.dto

import cn.sunline.saas.global.constant.RepaymentStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

data class DTOFeeItemAdd(
    val feeArrangementId: Long,
    val agreementId: Long,
    var feeAmount: BigDecimal,
    var repaymentAmount: BigDecimal = BigDecimal.ZERO,
    val feeRepaymentDate: Date? = null,
    val feeFromDate: Date,
    val feeToDate: Date,
    val feeUser: String?,
    val currencyType: CurrencyType,
)
