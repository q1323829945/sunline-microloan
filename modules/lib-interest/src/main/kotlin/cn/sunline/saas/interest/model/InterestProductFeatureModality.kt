package cn.sunline.saas.interest.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import jdk.jfr.Frequency
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
    indexes = [Index(
        name = "idx_interest_product_feature_modality_feature_id",
        columnList = "interest_product_feature_id"
    )]
)
class InterestProductFeatureModality(
    @Id
    val id: Long? = null,

    @NotNull
    @Column(name = "calculation_method", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var calculationMethod: String,

    @NotNull
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var frequency: String,

    @NotNull
    @Column(name = "interest_product_feature_id", nullable = false, columnDefinition = "bigint not null")
    val interestProductFeatureId: Long
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
