package cn.sunline.saas.invoice.model.db

import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InvoiceLine
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 16:51
 */
@Entity
@Table(name = "invoice_line")
class InvoiceLine(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "invoice_amount_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val invoiceAmountType: InvoiceAmountType,

    @NotNull
    @Column(name = "invoice_amount", nullable = false, scale = 19, precision = 2, columnDefinition = "number(19,2) not null")
    var invoiceAmount: BigDecimal
) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}