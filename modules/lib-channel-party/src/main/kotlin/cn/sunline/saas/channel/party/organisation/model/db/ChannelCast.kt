package cn.sunline.saas.channel.party.organisation.model.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(
    name = "channel_cast"
)
@EntityListeners(TenantListener::class)
class ChannelCast(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "channel_code", length = 64, nullable = false, columnDefinition = "varchar(64) ")
    var channelCode: String,

    @NotNull
    @Column(name = "channel_name", length = 128, nullable = false, columnDefinition = "varchar(128) ")
    var channelName: String,

    @NotNull
    @Column(name = "channel_cast_type", length = 32, columnDefinition = "varchar(32) not null")
    val channelCastType: ChannelCastType,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    var dateTime: Date,
) : MultiTenant {

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