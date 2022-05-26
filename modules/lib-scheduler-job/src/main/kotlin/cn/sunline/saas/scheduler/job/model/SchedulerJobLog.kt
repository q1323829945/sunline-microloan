package cn.sunline.saas.scheduler.job.model

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: SchedulerJobLog
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 9:59
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(name = "scheduler_job_log")
class SchedulerJobLog(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "scheduler_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val schedulerType: SchedulerType,

    @Temporal(TemporalType.TIMESTAMP)
    var schedulerTime: Date? = null,

    @NotNull
    @Column(name = "actor_id", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val actorId: String,

    @NotNull
    @Column(name = "actor_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val actorType: String,

    @NotNull
    @Column(name = "task_id", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val taskId: String,

    @Temporal(TemporalType.TIMESTAMP)
    val accountDate: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    var executeDate: Date? = null,

    @Column(name = "execute_context", columnDefinition = "blob")
    @Lob
    var executeContext: String? = null,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "job_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var jobStatus: JobStatus = JobStatus.AWAIT,

    @Temporal(TemporalType.TIMESTAMP)
    var completedDate: Date? = null,

    @Column(name = "error_context", nullable = true, length = 128, columnDefinition = "varchar(128) not null")
    var errorContext: String? = null
): MultiTenant {

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


