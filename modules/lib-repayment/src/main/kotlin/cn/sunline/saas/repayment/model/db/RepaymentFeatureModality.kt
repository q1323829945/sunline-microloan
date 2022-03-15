package cn.sunline.saas.repayment.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.repayment.model.PaymentMethodType
import cn.sunline.saas.repayment.model.RepaymentDayType
import cn.sunline.saas.repayment.model.RepaymentFrequency
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
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var frequency: RepaymentFrequency,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var repaymentDayType: RepaymentDayType

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
