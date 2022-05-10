package cn.sunline.saas.loan.product.model.db

import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.rule.engine.model.Condition
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: LoanProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 9:30
 */
@Entity
@Table(
    name = "loan_product",
)
class LoanProduct(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "identification_code", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val identificationCode: String,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var version: String,

    @NotNull
    @Column(nullable = false, length = 512, columnDefinition = "varchar(512) not null")
    var description: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "loan_product_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val loanProductType: LoanProductType,

    @NotNull
    @Column(name = "loan_purpose", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var loanPurpose: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "loan_product_upload_configure_mapping",
        joinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "loan_upload_configure_id", referencedColumnName = "id")]
    )
    var loanUploadConfigureFeatures: MutableList<LoanUploadConfigure>? = mutableListOf()
) : MultiTenant {

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "reference_id")
    var configurationOptions: MutableList<Condition> = mutableListOf()

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