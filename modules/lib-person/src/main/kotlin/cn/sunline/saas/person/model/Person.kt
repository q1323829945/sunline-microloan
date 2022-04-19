package cn.sunline.saas.person.model

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Person
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 16:23
 */
@Entity
@Table(name = "person")
class Person(
    @Id
    val id: Long,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "person_id")
    val personIdentifications: MutableList<PersonIdentification>,

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    val personName: PersonName,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "residential_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var residentialStatus: ResidentialStatus,

    @NotNull
    @Column(name = "birth_date", nullable = false)
    val birthDate: Instant,

    @NotNull
    @Column(name = "nationality", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var nationality: CountryType,

    @Column(name = "ethnicity", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var ethnicity: String


) : MultiTenant {

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