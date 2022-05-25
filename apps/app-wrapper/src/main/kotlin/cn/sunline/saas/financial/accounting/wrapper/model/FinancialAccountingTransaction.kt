package cn.sunline.saas.financial.accounting.wrapper.model

import cn.sunline.saas.global.constant.TransactionStatus
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.Date
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: BankingTransaction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 17:18
 */
@Entity
@Table(
    name = "financial_accounting_transaction"
)
class FinancialAccountingTransaction(
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
    @Temporal(TemporalType.TIMESTAMP)
    val initiatedDate: Date,

    @Column(name = "executed_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    val executedDate: Date?,

    @Column(name = "transaction_description", nullable = false, length = 128, columnDefinition = "varchar(128) null")
    val transactionDescription: String?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val transactionStatus: TransactionStatus,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val currency: CurrencyType,

    @NotNull
    @Column(nullable = false, scale = 19, precision = 2, columnDefinition = "number(19,2) not null")
    val amount: BigDecimal,

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