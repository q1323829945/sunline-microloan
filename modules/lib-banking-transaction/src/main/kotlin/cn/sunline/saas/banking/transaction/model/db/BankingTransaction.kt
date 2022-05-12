package cn.sunline.saas.banking.transaction.model.db

import cn.sunline.saas.global.constant.TransactionStatus
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: BankingTransaction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 17:18
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "banking_transaction"
)
class BankingTransaction(
    @Id
    val id: Long,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    val name: String,

    @NotNull
    @Column(name = "agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Column(name = "instruction_id", nullable = false, columnDefinition = "bigint not null")
    val instructionId: Long,

    @NotNull
    @Column(name = "initiated_date", nullable = false)
    val initiatedDate: Instant,

    @Column(name = "executed_date", nullable = true)
    var executedDate: Instant?,

    @Column(name = "transaction_description", nullable = true, length = 128, columnDefinition = "varchar(128) null")
    val transactionDescription: String?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var transactionStatus: TransactionStatus,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val currency: CurrencyType,

    @NotNull
    @Column(nullable = false, scale = 19, precision = 2, columnDefinition = "number(19,2) not null")
    val amount: BigDecimal,

    @Column(
        name = "applied_fee",
        nullable = true,
        scale = 19,
        precision = 2,
        columnDefinition = "number(19,2) null"
    )
    val appliedFee: BigDecimal?,

    @Column(
        name = "applied_rate",
        nullable = true,
        scale = 9,
        precision = 6,
        columnDefinition = "number(9,6) null"
    )
    val appliedRate: BigDecimal?,

    @NotNull
    @Column(name = "business_unit", nullable = false, columnDefinition = "bigint not null")
    val businessUnit: Long

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