package cn.sunline.saas.repayment.model.db

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: RepaymentProductFeatureModality
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 13:35
 */
@Entity
@Table(
    name = "repayment_feature_modality",
)
class RepaymentFeatureModality(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var paymentMethod: PaymentMethodType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "frequency", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var frequency: RepaymentFrequency,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "repayment_day_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var repaymentDayType: RepaymentDayType,

    @NotNull
    @Column(name = "grace_days", columnDefinition = "tinyint(1) not null")
    var graceDays: Int = 0

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
