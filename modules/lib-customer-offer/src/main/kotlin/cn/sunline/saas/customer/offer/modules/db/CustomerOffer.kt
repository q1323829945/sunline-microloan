package cn.sunline.saas.customer.offer.modules.db

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.joda.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "customer_offer",
        indexes = [
            Index(name = "idx_customer_id", columnList = "customer_id")
        ]
)
@EntityListeners(TenantListener::class)
class CustomerOffer (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "customer_id", columnDefinition = "bigint not null")
    var customerId:Long,

    @NotNull
    @Column(name = "product_id", columnDefinition = "bigint not null")
    var productId:Long,

    @NotNull
    @Column(name = "product_name", columnDefinition = "varchar(256) not null")
    var productName:String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 256, columnDefinition = "varchar(256) not null")
    var status:ApplyStatus = ApplyStatus.RECORD,

    @NotNull
    @Column(name = "data", columnDefinition = "text not null")
    var data:String,

    @NotNull
    var datetime:Instant,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null
): MultiTenant {
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