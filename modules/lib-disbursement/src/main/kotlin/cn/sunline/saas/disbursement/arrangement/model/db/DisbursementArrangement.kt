package cn.sunline.saas.disbursement.arrangement.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: DisbursementArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/18 15:48
 */
@Entity
@Table(
    name = "disbursement_arrangement",
    indexes = [Index(name = "idx_disbursement_arrangement_agreement_id", columnList = "agreement_id")]
)
class DisbursementArrangement(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "disbursement_account_type",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val disbursementAccountType: DisbursementAccountType,

    @NotNull
    @Column(name = "disbursement_account", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val disbursementAccount: String,

    @NotNull
    @Column(name = "disbursement_account_bank", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val disbursementAccountBank: String

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