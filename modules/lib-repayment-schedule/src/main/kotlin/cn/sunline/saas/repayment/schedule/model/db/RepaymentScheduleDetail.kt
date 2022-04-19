package cn.sunline.saas.repayment.schedule.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.jetbrains.annotations.NotNull
import org.joda.time.Instant
import java.math.BigDecimal
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "repayment_schedule_detail")
class RepaymentScheduleDetail (

     @Id
     var id : Long, // id

     @NotNull
     @Column(name = "repayment_schedule_id", nullable = false, columnDefinition = "bigint not null")
     var repaymentScheduleId : Long,// 期供计划主表Id

     @NotNull
     @Column(name = "period", nullable = false, columnDefinition = "int not null")
     var period : Int,// 当期期数

     @NotNull
     @Column(name = "installment", precision = 15, scale = 2, nullable = false, columnDefinition = "decimal(15,2) not null")
     var installment : BigDecimal, // 当期还款金额

     @NotNull
     @Column(name = "principal", precision = 15, scale = 2,  nullable = false, columnDefinition = "decimal(15,2) not null")
     var principal : BigDecimal, // 当期应还本金

     @NotNull
     @Column(name = "interest", precision = 15, scale = 2,  nullable = false, columnDefinition = "decimal(15,2) not null")
     var interest : BigDecimal, // 当期应还利息

     @NotNull
     @Column(name = "repayment_date", nullable = false)
     var repaymentDate : Instant,

     @CreationTimestamp
     @Temporal(TemporalType.TIMESTAMP)
     var created : Date? = null,

     @UpdateTimestamp
     @Temporal(TemporalType.TIMESTAMP)
     var updated : Date? = null

): MultiTenant {

     @javax.validation.constraints.NotNull
     @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
     private var tenantId: Long = 0L

     override fun getTenantId(): Long {
          return tenantId
     }

     override fun setTenantId(o: Long) {
          tenantId = o
     }
}

