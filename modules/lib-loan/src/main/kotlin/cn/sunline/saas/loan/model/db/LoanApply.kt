package cn.sunline.saas.loan.model.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "loan_apply",
    indexes = [
        Index(name = "idx_created", columnList = "created")
    ]
)
@EntityListeners(TenantListener::class)
class LoanApply(
    @Id
    @Column(name = "id", columnDefinition = "bigint not null")
    val applicationId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val productType: ProductType,

    @Enumerated(value = EnumType.STRING)
    @Column(length = 64, columnDefinition = "varchar(64) ")
    var term: LoanTermType?,

    @Column(columnDefinition = "decimal(21,2)")
    var amount: BigDecimal?,

    @NotNull
    @Column(name = "data", nullable = false, columnDefinition = "text not null")
    var data:String,


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null,

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