package cn.sunline.saas.customer.offer.modules.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "customer_loan_apply",
        indexes = [
                Index(name = "idx_customer_offer_id", columnList = "customer_offer_id")
        ]
)
class CustomerLoanApply(
        @Id
        var id: Long? = null,

        @NotNull
        @Column(name = "customer_offer_id", nullable = false, columnDefinition = "bigint not null")
        var customerOfferId:Long,

        @NotNull
        @Column(name = "data", nullable = false, columnDefinition = "text not null")
        var data:String,

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var created: Date? = null,

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var updated: Date? = null,


)