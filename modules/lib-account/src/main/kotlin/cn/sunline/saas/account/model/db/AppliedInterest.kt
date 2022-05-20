package cn.sunline.saas.account.model.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: AppliedInterest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/18 14:30
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(name="applied_interest")
class AppliedInterest(
    @Id
    val id:Long,

    @NotNull
    @Column(name="agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId:Long,

    @NotNull
    @Column(nullable = false, scale = 19, precision = 2, columnDefinition = "decimal(19,2) not null")
    val principal: BigDecimal,

    @NotNull
    @Column(
        name = "applied_rate",
        nullable = false,
        scale = 9,
        precision = 6,
        columnDefinition = "decimal(9,6) not null"
    )
    val appliedRate: BigDecimal,

    @NotNull
    @Column(
        name = "applied_interest",
        nullable = false,
        scale = 19,
        precision = 2,
        columnDefinition = "decimal(19,2) not null"
    )
    val appliedInterest: BigDecimal

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