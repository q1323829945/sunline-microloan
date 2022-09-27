package cn.sunline.saas.channel.statistics.modules.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "commission_detail")
@EntityListeners(TenantListener::class)
class CommissionDetail (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "channel_code", columnDefinition = "varchar(256) not null")
    val channelCode: String,

    @Column(name = "channel_name",length = 128,nullable = false,  columnDefinition = "varchar(128) ")
    var channelName: String,

    @NotNull
    @Column(name = "application_id", columnDefinition = "bigint not null")
    val applicationId: Long,

    @Column(name = "commission_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) null")
    var commissionAmount: BigDecimal,

    @Column(name = "ratio",scale = 19,precision = 2, columnDefinition = "decimal(19,2) null")
    var ratio: BigDecimal?,

    @Column(name = "statistics_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) null")
    var statisticsAmount: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency",  length = 32, columnDefinition = "varchar(32) null")
    val currency: CurrencyType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 256, columnDefinition = "varchar(256) not null")
    var status: ApplyStatus,

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