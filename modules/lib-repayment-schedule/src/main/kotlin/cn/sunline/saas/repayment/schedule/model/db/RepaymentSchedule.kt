package cn.sunline.saas.repayment.schedule.model.db

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "repayment_schedule")
class RepaymentSchedule(

    @Id
    var id: Long, // id

    @NotNull
    @Column(name = "installment", precision = 15, scale = 2, nullable = false, columnDefinition = "decimal(15,2) not null")
    var installment: BigDecimal,// 贷款金额 installment

    @NotNull
    @Column(name = "interest_Rate", precision = 9, scale = 6, nullable = false, columnDefinition = "decimal(9,6) not null")
    var interestRate: BigDecimal, // 贷款利率

    @NotNull
    @Column(name = "term", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    var term: LoanTermType, // 贷款期限

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "repayment_schedule_id")
//    @NotFound(action= NotFoundAction.IGNORE)
    @OrderBy("id ASC")
    var schedule: MutableList<RepaymentScheduleDetail> = mutableListOf(),

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

): MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}