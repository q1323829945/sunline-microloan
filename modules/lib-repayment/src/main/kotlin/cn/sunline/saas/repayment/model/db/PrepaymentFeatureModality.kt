package cn.sunline.saas.repayment.model.db

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.repayment.model.PrepaymentType
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: PrepaymentProductFeatureModality
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 13:35
 */
@Entity
@Table(
    name = "prepayment_feature_modality",
)
class PrepaymentFeatureModality(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var term: LoanTermType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var type: PrepaymentType,

    @NotNull
    @Column(name="penalty_ratio",nullable = false, scale = 9, precision = 6, columnDefinition = "number(9,6) not null")
    var penaltyRatio: BigDecimal?

) : MultiTenant {

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
