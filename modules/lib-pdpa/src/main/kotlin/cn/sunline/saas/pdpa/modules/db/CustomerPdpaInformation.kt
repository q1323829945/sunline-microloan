package cn.sunline.saas.pdpa.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "customer_pdpa_information",

)
@EntityListeners(TenantListener::class)
class CustomerPdpaInformation(
    @Id
    @Column(name="customer_id",updatable = false,  columnDefinition = "bigint null")
    val customerId: Long,

    @Column(name="pdpa_id", columnDefinition = "bigint null")
    var pdpaId: Long? = null,

    @Column(name = "electronic_signature",length = 1024, columnDefinition = "varchar(1024)  default null")
    var electronicSignature: String? = null,

    @Column(name = "face_recognition", length = 1024,  columnDefinition = "varchar(1024)  default null")
    var faceRecognition : String? = null,

    @Column(name = "fingerprint",length = 1024, columnDefinition = "varchar(1024)  default null")
    var fingerprint: String? = null,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null
) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id",columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }

}