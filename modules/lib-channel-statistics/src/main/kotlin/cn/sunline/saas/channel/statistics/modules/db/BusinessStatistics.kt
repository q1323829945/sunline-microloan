package cn.sunline.saas.channel.statistics.modules.db

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "business_statistics",
    indexes = [
        Index(name = "idx_business_statistics_unique", columnList = "customer_id,currency,year,month,day,frequency,tenant_id",unique = true)
    ]
)
@EntityListeners(TenantListener::class)
class BusinessStatistics  (
    @Id
    val id: Long? = null,

    @NotNull
    @Column(name = "customer_id",  columnDefinition = "bigint not null")
    val customerId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "frequency",length = 128, columnDefinition = "varchar(128) not null")
    val frequency: Frequency,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency",  length = 32, columnDefinition = "varchar(32) not null")
    val currency: CurrencyType?,

    @NotNull
    @Column(name = "repayment_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    val repaymentAmount: BigDecimal,

    @NotNull
    @Column(name = "payment_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    val paymentAmount: BigDecimal,

    @NotNull
    @Column(name = "year", columnDefinition = "bigint not null")
    val year: Long,

    @NotNull
    @Column(name = "month", columnDefinition = "bigint not null")
    val month: Long,

    @NotNull
    @Column(name = "day",  columnDefinition = "bigint not null")
    val day: Long,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    val datetime: Date,

    ): MultiTenant {

    @NotNull
    @Column(name = "tenant_id", columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}