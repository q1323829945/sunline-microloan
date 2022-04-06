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
     @Column(name = "repayment_schedule_id", nullable = false)
     var repaymentScheduleId : Long,// 期供计划主表Id

     @NotNull
     @Column(name = "period", nullable = false)
     var period : Int,// 当期期数

     @NotNull
     @Column(name = "repayment_installment", precision = 15, scale = 2, nullable = false)
     var repaymentInstallment : BigDecimal, // 当期还款金额

     @NotNull
     @Column(name = "principal", precision = 15, scale = 2,  nullable = false)
     var principal : BigDecimal, // 当期应还本金

     @NotNull
     @Column(name = "interest", precision = 15, scale = 2,  nullable = false)
     var interest : BigDecimal, // 当期应还利息

     @NotNull
     @Column(name = "remain_principal", precision = 15, scale = 2,  nullable = false)
     var remainPrincipal : BigDecimal, // 当期剩余本金

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

