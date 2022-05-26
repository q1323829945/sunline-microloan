package cn.sunline.saas.scheduler.job.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: SchedulerTask
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 10:40
 */
@Entity
@Table(name = "scheduler_task")
class SchedulerTask(
    @Id
    val id: Long,

    @Temporal(TemporalType.TIMESTAMP)
    var schedulerTime: Date? = null,

    @NotNull
    @Column(name = "actor_id", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val actorId: String,

    @NotNull
    @Column(name = "actor_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val actorType: String

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