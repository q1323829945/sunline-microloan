package cn.sunline.saas.loan.agreement.model.db

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: LoanAgreement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 18:32
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "loan_agreement"
)
class LoanAgreement(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val agreementType: AgreementType = AgreementType.PRODUCT_SALE,

    @NotNull
    val signedDate: Instant,

    @NotNull
    val fromDateTime: Instant,

    @NotNull
    val toDateTime: Instant,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val term: LoanTermType,

    @NotNull
    @Column(nullable = false, length = 2, columnDefinition = "tinyint(2) not null")
    val version: Int,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var status: AgreementStatus,

    @NotNull
    @Column(nullable = false, precision = 19, scale = 2, columnDefinition = "decimal(19,2) not null")
    val amount: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 3, columnDefinition = "varchar(3) not null")
    val currency: CurrencyType,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    val productId: Long,

    @Column(nullable = true, length = 128, columnDefinition = "varchar(128) null")
    var agreementDocument: String?,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "agreement_id")
    val involvements: MutableList<LoanAgreementInvolvement>,

    @Column(name = "purpose", nullable = true, columnDefinition = "varchar(64) null")
    val purpose: String?,

    @NotNull
    @Column(name = "application_id", nullable = false, columnDefinition = "bigint not null")
    val applicationId: Long,

    @NotNull
    @Column(name = "user_id", nullable = false, columnDefinition = "bigint not null")
    val userId: Long

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