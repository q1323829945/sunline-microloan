package cn.sunline.saas.multi_tenant.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * @title: Permission
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 13:43
 */
@Entity
@Table(
    name = "tenant_permission"
)
class TenantPermission (

    @Id
    val appID: Long?,

    @NotNull
    @Column(nullable = false, columnDefinition = "bigint not null")
    var tenantId:Long,

    @NotNull
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default false")
    var enabled: Boolean = false,
)