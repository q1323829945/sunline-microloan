package cn.sunline.saas.underwriting.arrangement.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.underwriting.arrangement.model.ArrangementLifecycleStatus
import org.joda.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: UnderwritingArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 18:32
 */
@Entity
@Table(
    name = "underwriting_arrangement"
)
class UnderwritingArrangement(
    @Id
    val id: Long,

    @NotNull
    @Column(nullable = false, columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    val startDate: Instant,

    @NotNull
    val endDate: Instant,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val status: ArrangementLifecycleStatus,

    @Column(nullable = true, length = 128, columnDefinition = "varchar(128) null")
    var agreementDocument: String?,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "arrangement_id")
    val involvements: MutableList<UnderwritingArrangementInvolvement>

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