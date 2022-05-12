package cn.sunline.saas.account.model.db

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: AccountEntry
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 10:18
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "account_entry"
)
class AccountEntry(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "account_id", nullable = false, columnDefinition = "bigint not null")
    val accountId: Long,

    @NotNull
    @Column(name = "account_balance",nullable = false,scale = 19,precision = 2,columnDefinition = "decimal(19,2) not null")
    val accountEntryAmount: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_entry_currency", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val accountEntryCurrency: CurrencyType,

    @Column(name = "account_purpose", nullable = true, length = 128, columnDefinition = "varchar(128) null")
    val accountPurpose: String?,

    @NotNull
    @Column(name = "account_entry_value_date", nullable = false)
    val accountEntryValueDate: Instant

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