package cn.sunline.saas.loan.model.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "loan_agent"
)
@EntityListeners(TenantListener::class)
class LoanAgent(
    @Id
    @Column(name = "id", columnDefinition = "bigint not null")
    val applicationId: Long,

    @Column(name = "seq", columnDefinition = "varchar(64) default null")
    val seq:String? = null,

    @Column(length = 64, columnDefinition = "varchar(64) default null")
    var name: String?,

    @NotNull
    @Column(name = "data", nullable = false, columnDefinition = "text not null")
    var data:String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 256, columnDefinition = "varchar(256) not null")
    var status: ApplyStatus = ApplyStatus.RECORD,

    @NotNull
    @Column(name = "product_id", nullable = true, columnDefinition = "bigint default null")
    var productId: Long? = null,

    @NotNull
    @Column(name = "channel_code",length = 64,nullable = false,  columnDefinition = "varchar(64) ")
    var channelCode: String,

    @NotNull
    @Column(name = "channel_name",length = 128,nullable = false,  columnDefinition = "varchar(128) ")
    var channelName: String,


    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    var handle: LoanApplyHandle? = null,


    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    var loanApply: LoanApply? = null,

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