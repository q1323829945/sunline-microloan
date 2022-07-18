package cn.sunline.saas.pdpa.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "pdpa_authority",
    indexes = [
        Index(name = "idx_tenant_unique", columnList = "tenant_id",unique = true),
    ]
)
@EntityListeners(TenantListener::class)
class PdpaAuthority(
    @Id
    var id: Long,

    @Column(name = "is_electronic_signature",columnDefinition = "tinyint(1)  default false")
    @NotNull
    var isElectronicSignature: Boolean = false,

    @Column(name = "is_face_recognition", columnDefinition = "tinyint(1)  default false")
    @NotNull
    var isFaceRecognition : Boolean = false,

    @Column(name = "is_fingerprint",columnDefinition = "tinyint(1)  default false")
    @NotNull
    var isFingerprint: Boolean = false,

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