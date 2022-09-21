package cn.sunline.saas.statistics.modules.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "loan_application_detail")
@EntityListeners(TenantListener::class)
class LoanApplicationDetail(
    @Id
    var id: Long? = null,

    @NotNull
    @Column(name = "channel_code", columnDefinition = "varchar(256) not null")
    val channelCode: String,

    @NotNull
    @Column(name = "channel_name",length = 128,nullable = false,  columnDefinition = "varchar(128) ")
    var channelName: String,

    @NotNull
    @Column(name = "product_id", columnDefinition = "bigint not null")
    val productId: Long,

    @NotNull
    @Column(name = "product_name", columnDefinition = "varchar(256) not null")
    val productName: String,

    @NotNull
    @Column(name = "application_id", columnDefinition = "bigint not null")
    val applicationId: Long,

    @NotNull
    @Column(name = "apply_amount", scale = 19, precision = 2, columnDefinition = "decimal(19,2) not null")
    var applyAmount: BigDecimal,

    @NotNull
    @Column(name = "approval_amount", scale = 19, precision = 2, columnDefinition = "decimal(19,2) not null")
    var approvalAmount: BigDecimal,


    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency",  length = 32, columnDefinition = "varchar(32) not null")
    val currency: CurrencyType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 256, columnDefinition = "varchar(256) not null")
    var status: ApplyStatus,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var datetime: Date,

    ) : MultiTenant {

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