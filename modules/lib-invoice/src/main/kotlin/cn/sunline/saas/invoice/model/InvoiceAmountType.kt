package cn.sunline.saas.invoice.model

/**
 * @title: InvoiceAmountType
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 16:53
 */
enum class InvoiceAmountType(val order: Int) {
    PRINCIPAL(3), INTEREST(2), FEE(4), PENALTY_INTEREST(1)

}