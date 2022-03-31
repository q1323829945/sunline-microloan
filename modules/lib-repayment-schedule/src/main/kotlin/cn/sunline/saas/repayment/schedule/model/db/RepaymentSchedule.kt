package cn.sunline.saas.repayment.schedule.model.db

import cn.sunline.saas.repayment.model.PaymentMethodType
import cn.sunline.saas.repayment.model.RepaymentFrequency
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
    @Column(name = "repayment_schedule_id", nullable = false)
    var repaymentScheduleId: Long, // id

    @NotNull
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    var amount: BigDecimal,// 贷款金额 installment

    @NotNull
    @Column(name = "term", nullable = false)
    var term: Int,// 贷款期限

    @NotNull
    @Column(name = "interest_Rate", precision = 9, scale = 6, nullable = false)
    var interestRate: BigDecimal, // 贷款利率

    @NotNull
    @Column(name = "repayment_type", length = 8, nullable = false)
    var paymentMethod: PaymentMethodType, //还款类型

    @NotNull
    @Column(name = "total_interest", precision = 15, scale = 2, nullable = false)
    var totalInterest: BigDecimal,// 总利息金额

    @NotNull
    @Column(name = "total_repayment", precision = 15, scale = 2, nullable = false)
    var totalRepayment: BigDecimal,// 总还款金额

    @Column(name = "repayment_frequency", nullable = true)
    var repaymentFrequency: RepaymentFrequency? = null,// 频率

    @OneToMany(fetch = FetchType.EAGER, targetEntity = RepaymentScheduleDetail::class, mappedBy = "repaymentScheduleId")
    @NotFound(action= NotFoundAction.IGNORE)
    @OrderBy("id ASC")
    var repaymentScheduleDetail: MutableList<RepaymentScheduleDetail> = mutableListOf(),

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

)
