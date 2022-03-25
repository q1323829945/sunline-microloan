package cn.sunline.saas.customer.offer.modules.db

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "customer_offer",
        indexes = [
            Index(name = "idx_customer_id", columnList = "customer_id")
        ]
)
class CustomerOffer (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "customer_id", nullable = false, columnDefinition = "bigint not null")
    var customerId:Long,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    var productId:Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var status:ApplyStatus = ApplyStatus.RECORD,

    @Column(name = "data", nullable = false, columnDefinition = "text ")
    var data:String?,

    @NotNull
    @Column(name = "datetime", nullable = false,length = 256,  columnDefinition = "varchar(256) not null")
    var datetime:String,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null
)