package cn.sunline.saas.customer.offer.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "customer_loan_apply"
)
@EntityListeners(TenantListener::class)
class CustomerLoanApply(
        @Id
        @Column(name = "customer_offer_id")
        var customerOfferId:Long?,

        @Column(name = "amount", nullable = false, columnDefinition = "decimal(21,2)")
        var amount:BigDecimal?,

        @NotNull
        @Column(name = "data", nullable = false, columnDefinition = "text not null")
        var data:String,

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var created: Date? = null,

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var updated: Date? = null,

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