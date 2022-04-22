package cn.sunline.saas.repayment.arrangement.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * @title: RepaymentAccount
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/20 11:04
 */
@Entity
@Table(name = "repayment_account")
class RepaymentAccount(
    @Id val id: Long,

    @NotNull
    @Column(name = "repayment_account", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val repaymentAccount: String,

    @NotNull
    @Column(
        name = "repayment_account_bank",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val repaymentAccountBank: String
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