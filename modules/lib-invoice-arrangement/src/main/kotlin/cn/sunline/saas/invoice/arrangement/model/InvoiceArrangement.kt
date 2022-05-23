package cn.sunline.saas.invoice.arrangement.model

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InvoiceArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/18 14:43
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "invoice_arrangement"
)
class InvoiceArrangement(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "invoice_day", nullable = false)
    var invoiceDay: Instant,

    @NotNull
    @Column(name = "repayment_day", nullable = false)
    var repaymentDay: Instant,

    @NotNull
    @Column(name = "grace_days", nullable = false, columnDefinition = "tinyint not null")
    var graceDays: Int = 0

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