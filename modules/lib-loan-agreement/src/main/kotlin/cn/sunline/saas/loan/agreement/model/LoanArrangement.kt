package cn.sunline.saas.loan.agreement.model

import cn.sunline.saas.abstract.core.agreement.Arrangement
import cn.sunline.saas.abstract.core.agreement.ArrangementStatus
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: LoanArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 18:12
 */
@Entity
@Table(
    name = "loan_arrangement",
    indexes = [Index(name = "idx_loan_arrangement_agreement_id", columnList = "agreement_id")]
)
class LoanArrangement(
    id: Long? = null,
    agreementId: Long,
    startDate: Date = DateTime.now().toDate(),
    endDate: Date,
    status: ArrangementStatus,

    @NotNull @Temporal(TemporalType.TIMESTAMP) var maturityDate: Date,

    @NotNull @Column(
        nullable = false,
        precision = 19,
        scale = 2,
        columnDefinition = "decimal(19,2) not null"
    ) var amount: BigDecimal = BigDecimal.ZERO
) : Arrangement(id, agreementId, startDate, endDate, status), MultiTenant {

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