package cn.sunline.saas.fee.arrangement.model.db

import cn.sunline.saas.global.constant.RepaymentStatus
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(
    name = "fee_item"
)
@EntityListeners(TenantListener::class)
class FeeItem(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "fee_arrangement_id", nullable = false, columnDefinition = "bigint not null")
    val feeArrangementId: Long,

    @NotNull
    @Column(name = "agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Column(name = "fee_amount", nullable = false, scale = 19, precision = 2, columnDefinition = "decimal(19,2) not null" )
    var feeAmount: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 3, columnDefinition = "varchar(3) not null")
    val currency: CurrencyType,

    @NotNull
    @Column(name = "repayment_amount", nullable = false, scale = 19, precision = 2, columnDefinition = "decimal(19,2) not null" )
    var repaymentAmount: BigDecimal = BigDecimal.ZERO,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_repayment_date")
    var feeRepaymentDate: Date?,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fee_from_date")
    val feeFromDate: Date?,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fee_to_date")
    val feeToDate: Date?,

    @NotNull
    @Column(name = "fee_user", nullable = false, columnDefinition = "varchar(32) null")
    val feeUser: String?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "repayment_status", nullable = false, columnDefinition = "varchar(32) not null")
    var repaymentStatus: RepaymentStatus = RepaymentStatus.UNDO

    ) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}