package cn.sunline.saas.channel.statistics.modules.db

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.channel.statistics.modules.TransactionType
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "business_detail")
@EntityListeners(TenantListener::class)
class BusinessDetail  (
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "agreement_id", columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Column(name = "customer_id",  columnDefinition = "bigint not null")
    val customerId: Long,

    @NotNull
    @Column(name = "amount",scale = 19,precision = 2, columnDefinition = "decimal(19,2) not null")
    var amount: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency",  length = 32, columnDefinition = "varchar(32) null")
    val currency: CurrencyType?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_type",  length = 32, columnDefinition = "varchar(32) not null")
    val transactionType: TransactionType,

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