package cn.sunline.saas.statistics.modules.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.Frequency
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
    @Column(name = "channel", columnDefinition = "varchar(256) not null")
    val channel: String,

    @NotNull
    @Column(name = "application_id", columnDefinition = "bigint not null")
    val applicationId: Long,

    @NotNull
    @Column(name = "amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    var amount: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency",  length = 32, columnDefinition = "varchar(32) not null")
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