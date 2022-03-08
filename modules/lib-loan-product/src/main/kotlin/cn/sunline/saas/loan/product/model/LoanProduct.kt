package cn.sunline.saas.loan.product.model

import cn.sunline.saas.abstract.core.banking.product.BankingProduct
import cn.sunline.saas.abstract.core.banking.product.BankingProductStatus
import cn.sunline.saas.abstract.core.banking.product.feature.ConfigurationParameter
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import cn.sunline.saas.multi_tenant.model.MultiTenant
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
    indexes = [Index(name = "idx_loan_product_product_directory_id", columnList = "product_directory_id")]
)
class LoanProduct<T : ProductFeature>(
    id: Long? = null,
    identificationCode: String,
    name: String,
    version: String,
    description: String,
    status: BankingProductStatus = BankingProductStatus.INITIATED,
    configurationOptions: MutableList<ConfigurationParameter> = mutableListOf(),
    features: MutableList<T> = mutableListOf(),

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "loan_product_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val loanProductType: LoanProductType,

    @NotNull
    @Column(name = "loan_purpose", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var loanPurpose: String,

    @NotNull
    @Column(name = "product_directory_id", nullable = true, columnDefinition = "bigint not null")
    var productDirectoryId: Long?

) : BankingProduct<T>(id, identificationCode, name, version, description, status, configurationOptions, features),
    MultiTenant {

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