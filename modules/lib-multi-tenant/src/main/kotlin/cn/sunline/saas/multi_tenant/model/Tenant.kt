package cn.sunline.saas.multi_tenant.model

import cn.sunline.saas.global.model.CountryType
import java.util.UUID
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    @Enumerated(value = EnumType.STRING)
    val country: CountryType,

    @Column(nullable = false, columnDefinition = "tinyint(1) default false")
    @NotNull
    var enabled: Boolean = false,

    @NotNull
    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    var uuid: UUID = UUID.randomUUID(),

)