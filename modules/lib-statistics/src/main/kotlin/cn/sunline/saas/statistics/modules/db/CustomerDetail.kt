package cn.sunline.saas.statistics.modules.db

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "customer_detail")
@EntityListeners(TenantListener::class)
class CustomerDetail  (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "party_id", columnDefinition = "bigint not null")
    var partyId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "party_type",length = 128, columnDefinition = "varchar(128) not null")
    var partyType: PartyType,

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