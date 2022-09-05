package cn.sunline.saas.channel.product.model.db

import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "product",
)
@EntityListeners(TenantListener::class)
class Product(
    @Id
    val id: Long,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var productType: ProductType,

    @Column(length = 512, columnDefinition = "varchar(512)")
    var description: String? = null,

    @NotNull
    @Column(name = "rate_plan_id", columnDefinition = "bigint", nullable = false)
    var ratePlanId: Long,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "product_questionnaire_mapping",
        joinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "questionnaire_id", referencedColumnName = "id")]
    )
    var questionnaires: MutableList<Questionnaire> = mutableListOf()

) : MultiTenant {
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var status: BankingProductStatus = BankingProductStatus.INITIATED

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