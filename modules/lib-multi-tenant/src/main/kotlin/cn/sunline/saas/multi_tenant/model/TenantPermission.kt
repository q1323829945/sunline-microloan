package cn.sunline.saas.multi_tenant.model

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Permission
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 13:43
 */
@Entity
@Table(
    name = "tenant_permission",
    indexes = [
        Index(name = "idx_tenant_permission_tenant_id", columnList = "tenant_id")
    ]
)
class TenantPermission(
    @Id
    @Column(name = "product_application_id", nullable = false, columnDefinition = "varchar(32)")
    val productApplicationId: String,

    @NotNull
    @Column(name="tenant_id",nullable = false, columnDefinition = "bigint not null")
    var tenantId: Long,

    @NotNull
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default false")
    var enabled: Boolean = false,

    @Column(columnDefinition = "bigint null")
    val subscriptionId: Long?,
)