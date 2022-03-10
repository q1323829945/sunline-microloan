package cn.sunline.saas.loan.product.model.db

import cn.sunline.saas.abstract.core.banking.product.BankingProduct
import cn.sunline.saas.abstract.core.banking.product.BankingProductStatus
import cn.sunline.saas.interest.model.db.InterestProductFeature
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.repayment.model.RepaymentProductFeature
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
class LoanProduct(
    @Id
    val id: Long? = null,
    identificationCode: String,
    name: String,
    version: String,
    description: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "loan_product_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val loanProductType: LoanProductType,

    @NotNull
    @Column(name = "loan_purpose", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var loanPurpose: String,

    status: BankingProductStatus = BankingProductStatus.INITIATED

) : BankingProduct(),
    MultiTenant {

    @OneToOne(fetch = FetchType.EAGER)
    lateinit var interestFeature: InterestProductFeature

    @OneToOne(fetch = FetchType.EAGER)
    lateinit var repaymentFeature: RepaymentProductFeature

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