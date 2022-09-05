package cn.sunline.saas.channel.statistics.modules.db

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "customer_statistics",
    indexes = [
        Index(name = "idx_customer_statistics_unique", columnList = "year,month,day,frequency,tenant_id",unique = true)
    ]
)
@EntityListeners(TenantListener::class)
class CustomerStatistics  (
    @Id
    var id: Long? = null,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "frequency",length = 128, columnDefinition = "varchar(128) not null")
    var frequency: Frequency,

    @NotNull
    @Column(name = "person_count", columnDefinition = "bigint not null")
    var personCount: Long,

    @NotNull
    @Column(name = "organisation_count", columnDefinition = "bigint not null")
    var organisationCount: Long,

    @NotNull
    @Column(name = "party_count", columnDefinition = "bigint not null")
    var partyCount: Long,

    @NotNull
    @Column(name = "year", columnDefinition = "bigint not null")
    var year: Long,

    @NotNull
    @Column(name = "month", columnDefinition = "bigint not null")
    var month: Long,

    @NotNull
    @Column(name = "day",  columnDefinition = "bigint not null")
    var day: Long,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var datetime: Date,

    ): MultiTenant {

    @NotNull
    @Column(name = "tenant_id", columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}