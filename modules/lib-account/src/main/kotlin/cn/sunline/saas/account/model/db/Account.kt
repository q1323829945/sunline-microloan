package cn.sunline.saas.account.model.db

import cn.sunline.saas.account.model.AccountClass
import cn.sunline.saas.account.model.AccountStatus
import cn.sunline.saas.account.model.AccountType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Account
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/9 15:54
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "account"
)
class Account(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var accountStatus: AccountStatus,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val accountType: AccountType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_class", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val accountClass: AccountClass,

    @Column(name = "account_purpose", nullable = true, length = 128, columnDefinition = "varchar(128) null")
    val accountPurpose: String?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_currency", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val accountCurrency: CurrencyType,

    @OneToMany(fetch = FetchType.EAGER ,cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    val accountBalance: MutableList<AccountBalance>,

    @Column(name = "reference_account", nullable = true, columnDefinition = "bigint null")
    var referenceAccount:Long?

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