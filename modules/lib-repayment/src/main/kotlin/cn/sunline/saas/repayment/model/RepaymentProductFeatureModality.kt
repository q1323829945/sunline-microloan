package cn.sunline.saas.repayment.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InterestProductFeatureModality
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 13:35
 */
@Entity
@Table(
    name = "interest_product_feature_modality",
)
class RepaymentProductFeatureModality(
    @Id
    val id: Long? = null,

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
