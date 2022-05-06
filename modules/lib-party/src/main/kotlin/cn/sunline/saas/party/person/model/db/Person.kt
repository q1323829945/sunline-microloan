package cn.sunline.saas.party.person.model.db

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.party.person.model.ResidentialStatus
import org.joda.time.Instant
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
@EntityListeners(TenantListener::class)
class Person(
    @Id
    val id: Long,

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name= " id")
    @MapsId
    var personName: PersonName,

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
    var ethnicity: String,

    ) : MultiTenant {

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true , mappedBy = "personId")
    var personIdentifications: MutableList<PersonIdentification> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true , mappedBy = "personId")
    var personRoles: MutableList<PersonRole> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

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