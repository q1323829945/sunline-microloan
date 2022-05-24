package cn.sunline.saas.invoice.model.db

import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.dto.RepaymentStatus
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Invoice
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 11:25
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(name = "invoice", indexes = [Index(name = "idx_invoice_invoicee", columnList = "invoicee")])
class Invoice(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "invoice_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val invoiceType: InvoiceType,

    @NotNull
    @Column(name = "invoice_due_date", nullable = false)
    val invoiceRepaymentDate: Instant,

    @NotNull
    @Column(name = "invoice_period_from_date", nullable = false)
    val invoicePeriodFromDate: Instant,

    @NotNull
    @Column(name = "invoice_period_to_date", nullable = false)
    val invoicePeriodToDate: Instant,

    @NotNull
    @Column(name = "invoice_assigned_document", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val invoiceAssignedDocument:String,

    @NotNull
    @Column(name = "invoice_amount", nullable = false, scale = 19, precision = 2, columnDefinition = "number(19,2) not null")
    var invoiceAmount:BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "invoice_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var invoiceStatus: InvoiceStatus,

    @NotNull
    @Column(name = "invoicee", nullable = false, columnDefinition = "bigint not null")
    val invoicee:Long,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    val invoiceLines : MutableList<InvoiceLine>,

    @NotNull
    @Column(name = "agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId:Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "repayment_status", nullable = false, columnDefinition = "varchar(32) not null")
    var repaymentStatus: RepaymentStatus = RepaymentStatus.UNDO

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