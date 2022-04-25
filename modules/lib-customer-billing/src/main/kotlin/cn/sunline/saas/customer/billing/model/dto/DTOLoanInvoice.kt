package cn.sunline.saas.customer.billing.model.dto

import java.math.BigDecimal

/**
 * @title: DTOLoanInvoice
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 17:05
 */
data class DTOLoanInvoice(
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceAddress: String,
    val invoicee: Long,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val fee: BigDecimal
)
