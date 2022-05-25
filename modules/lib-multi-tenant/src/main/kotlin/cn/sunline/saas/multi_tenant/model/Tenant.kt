package cn.sunline.saas.multi_tenant.model

import cn.sunline.saas.global.model.CountryType
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Tenant
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 11:58
 */
@Entity
@Table(
    name = "tenant"
)
class Tenant(
    @Id
    val id: Long,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    @Enumerated(value = EnumType.STRING)
    val country: CountryType,

    @Column(nullable = false, columnDefinition = "tinyint(1) default false")
    @NotNull
    var enabled: Boolean = false,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tenantId")
    var permissions: MutableList<TenantPermission>? = mutableListOf()

)