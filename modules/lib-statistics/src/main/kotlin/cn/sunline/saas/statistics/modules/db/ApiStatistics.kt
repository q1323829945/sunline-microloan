package cn.sunline.saas.statistics.modules.db

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "api_statistics")
@EntityListeners(TenantListener::class)
class ApiStatistics  (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "api", length = 128, columnDefinition = "varchar(128) not null")
    var api: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "frequency",length = 128, columnDefinition = "varchar(128) not null")
    var frequency: Frequency,

    @NotNull
    @Column(name = "count", columnDefinition = "bigint not null")
    var count: Long,

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