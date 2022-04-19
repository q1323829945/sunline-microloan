package cn.sunline.saas.organisation.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: BusinessUnit
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 15:43
 */
@Entity
@Table(name = "business_unit")
class BusinessUnit(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val type: BusinessUnitType
): MultiTenant {

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