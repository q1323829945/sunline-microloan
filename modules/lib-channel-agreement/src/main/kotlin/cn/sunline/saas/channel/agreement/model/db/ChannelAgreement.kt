package cn.sunline.saas.channel.agreement.model.db

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.Date
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "channel_agreement"
)
class ChannelAgreement(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val agreementType: AgreementType = AgreementType.CHANNEL_ACCESS,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    val signedDate: Date,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    val fromDateTime: Date,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    val toDateTime: Date,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var status: AgreementStatus,

    @NotNull
    @Column(name = "channel_id", nullable = false, columnDefinition = "bigint not null")
    val channelId: Long,

    @NotNull
    @Column(nullable = false, length = 2, columnDefinition = "tinyint(2) not null")
    val version: Int,

    @Column(nullable = true, length = 128, columnDefinition = "varchar(128) null")
    var agreementDocument: String?,


    ) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}