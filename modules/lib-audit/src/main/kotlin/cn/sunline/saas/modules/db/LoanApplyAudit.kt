package cn.sunline.saas.modules.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "loan_apply_audit"
)
@EntityListeners(TenantListener::class)
class LoanApplyAudit (
    @Id
    val id:Long,

    @NotNull
    @Column(name = "application_id", nullable = false, columnDefinition = "varchar(64) " )
    val applicationId:String,

    @Column( length = 64, columnDefinition = "varchar(64) default null")
    val name: String?,

    @Column(name = "product_id",columnDefinition = "bigint default null")
    val productId: Long?,

    @Enumerated(value = EnumType.STRING)
    @Column(length = 64, columnDefinition = "varchar(64)")
    val term: LoanTermType?,

    @Column(columnDefinition = "decimal(21,2)")
    val amount: BigDecimal?,

    @NotNull
    @Column(name = "data", nullable = false, columnDefinition = "text not null")
    val data:String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 256, columnDefinition = "varchar(256) not null")
    val status: ApplyStatus,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    val created: Date? = null,

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