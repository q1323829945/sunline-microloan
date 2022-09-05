package cn.sunline.saas.channel.statistics.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.Date
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "api_detail")
@EntityListeners(TenantListener::class)
class ApiDetail (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "api", length = 128, columnDefinition = "varchar(128) not null")
    var api: String,

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