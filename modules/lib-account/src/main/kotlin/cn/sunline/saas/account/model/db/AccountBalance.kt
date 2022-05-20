package cn.sunline.saas.account.model.db

import cn.sunline.saas.account.model.AccountBalanceType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: AccountBalance
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 10:12
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "account_balance"
)
class AccountBalance(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "account_balance",nullable = false,scale = 19,precision = 2,columnDefinition = "decimal(19,2) not null")
    var accountBalance: BigDecimal,

    @NotNull
    @Column(name = "account_balance_date",nullable = false)
    var accountBalanceDate: Instant,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_balance_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val accountBalanceType: AccountBalanceType,

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