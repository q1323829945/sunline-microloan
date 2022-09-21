package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.channel.statistics.modules.TransactionType
import java.math.BigDecimal
import java.util.*

data class DTOBusinessDetail(
    val agreementId: Long,
    val customerId:Long,
    var amount: BigDecimal,
    val currency: CurrencyType?,
    var transactionType: TransactionType = TransactionType.PAYMENT,
)

data class DTOBusinessDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOBusinessCount(
    val customerId:Long,
    val paymentAmount: BigDecimal,
    val repaymentAmount:BigDecimal,
    val currency:CurrencyType?
)


