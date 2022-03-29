package cn.sunline.saas.customer.offer.modules.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "customer_loan_apply"
)
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


)