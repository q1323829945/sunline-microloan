package cn.sunline.saas.statistics.modules.db

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "business_statistics")
class BusinessStatistics  (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "customer_id",  columnDefinition = "bigint not null")
    val customerId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "frequency",length = 128, columnDefinition = "varchar(128) not null")
    var frequency: Frequency,

    @NotNull
    @Column(name = "total_amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    var totalAmount: BigDecimal,

    @NotNull
    @Column(name = "year", columnDefinition = "bigint not null")
    var year: Long,

    @NotNull
    @Column(name = "month", columnDefinition = "bigint not null")
    var month: Long,

    @NotNull
    @Column(name = "day",  columnDefinition = "bigint not null")
    var day: Long,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var datetime: Date,

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