package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.statistics.modules.TransactionType
import org.joda.time.DateTime
import java.math.BigDecimal


data class DTOBusinessStatistics(
    val customerId:Long,
    val paymentAmount: BigDecimal,
    val repaymentAmount:BigDecimal,
    val currencyType: CurrencyType,
    val frequency: Frequency,
)

data class DTOBusinessStatisticsFindParams(
    val customerId:Long,
    val datetime: DateTime,
    val frequency: Frequency,
    val currencyType: CurrencyType
)