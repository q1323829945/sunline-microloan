package cn.sunline.saas.channel.statistics.modules.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "commission_statistics")
@EntityListeners(TenantListener::class)
class CommissionStatistics(
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "channel_code", columnDefinition = "varchar(256) not null")
    val channelCode: String,

    @Column(name = "channel_name",length = 128,nullable = false,  columnDefinition = "varchar(128) ")
    var channelName: String,

    @NotNull
    @Column(name = "statistics_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    var statisticsAmount: BigDecimal,

    @NotNull
    @Column(name = "commission_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    var commissionAmount: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "frequency",length = 128, columnDefinition = "varchar(128) not null")
    var frequency: Frequency,

    @NotNull
    @Column(name = "apply_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    val applyStatus: ApplyStatus,

    @NotNull
    @Column(name = "year", columnDefinition = "bigint not null")
    val year: Long,

    @NotNull
    @Column(name = "month", columnDefinition = "bigint not null")
    val month: Long,

    @NotNull
    @Column(name = "day",  columnDefinition = "bigint not null")
    val day: Long,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var datetime: Date,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date,

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