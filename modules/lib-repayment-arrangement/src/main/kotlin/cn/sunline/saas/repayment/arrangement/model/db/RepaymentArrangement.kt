package cn.sunline.saas.repayment.arrangement.model.db

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: RepaymentArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 14:00
 */
@Entity
@Table(
    name = "repayment_arrangement"
)
class RepaymentArrangement(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    val paymentMethod: PaymentMethodType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val frequency: RepaymentFrequency,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val repaymentDayType: RepaymentDayType,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "repayment_id")
    val prepayment: MutableList<PrepaymentArrangement>,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "repayment_id")
    val repaymentAccounts: MutableList<RepaymentAccount>,

    @NotNull
    @Column(name="auto_repayment",nullable = false, columnDefinition = "tinyint not null")
    var autoRepayment:Boolean = true

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


