package cn.sunline.saas.customer.offer.modules.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import org.joda.time.Instant
import java.math.BigDecimal

data class DTOCustomerOfferPage(
        var customerOfferId:Long,
        var amount:BigDecimal?,
        val datetime: Long,
        var productName:String,
        val status: ApplyStatus
)
