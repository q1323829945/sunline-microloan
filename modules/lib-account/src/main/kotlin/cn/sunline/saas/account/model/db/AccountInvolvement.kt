package cn.sunline.saas.account.model.db

import cn.sunline.saas.account.model.AccountInvolvementType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: AccountInvolvement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 10:31
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "account_involvement",
    indexes = [Index(name = "idx_account_involvement_account_id", columnList = "account_id")]
)
class AccountInvolvement(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "account_id", nullable = false, columnDefinition = "bigint not null")
    val accountId: Long,

    @NotNull
    @Column(name = "account_involvement", nullable = false, columnDefinition = "bigint not null")
    val accountInvolvement: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "account_involvement_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val accountInvolvementType: AccountInvolvementType

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