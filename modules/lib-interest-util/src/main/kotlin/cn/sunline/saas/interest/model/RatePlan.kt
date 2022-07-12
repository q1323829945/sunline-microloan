package cn.sunline.saas.interest.model

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: RatePlan
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 11:09
 */
@Entity
@Table(
    name = "rate_plan",
)
@EntityListeners(TenantListener::class)
class RatePlan(
    @Id
    var id: Long? = null,

    @NotNull
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var name: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "rate_plan_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val type: RatePlanType = RatePlanType.STANDARD,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "rate_plan_id")
    var rates: MutableList<InterestRate> = mutableListOf()

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