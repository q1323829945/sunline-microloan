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
    name = "loan_apply_handle",
    indexes = [
        Index(name = "idx_supplement", columnList = "supplement")
    ]
)
@EntityListeners(TenantListener::class)
class LoanApplyHandle(
    @Id
    @Column(name = "id", columnDefinition = "bigint not null")
    val applicationId: Long,

    @NotNull
    @Column(length = 64, columnDefinition = "varchar(64) not null")
    var supplement: String,

    @Column(name = "supplement_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    var supplementDate: Date? = null,
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