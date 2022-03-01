package cn.sunline.saas.loan.agreement.model

import cn.sunline.saas.abstract.core.agreement.Agreement
import cn.sunline.saas.abstract.core.agreement.AgreementStatus
import cn.sunline.saas.abstract.core.agreement.AgreementType
import cn.sunline.saas.abstract.core.agreement.Arrangement
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.locks.Condition
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * @title: LoanAgreement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 18:32
 */
@Entity
@Table(
    name = "loan_agreement"
)
class LoanAgreement(
    id: Long? = null,
    agreementType: AgreementType,
    signedDate: Date = DateTime.now().toDate(),
    fromDateTime: Date = DateTime.now().toDate(),
    toDateTime: Date? = null,
    version: String,
    status: AgreementStatus,
    conditions: MutableList<Condition> = mutableListOf(),
    arrangements: MutableList<LoanArrangement> = mutableListOf(),

    @NotNull
    @Column(nullable = false, precision = 19, scale = 2, columnDefinition = "decimal(19,2) not null")
    var amount: BigDecimal = BigDecimal.ZERO
) : Agreement<LoanArrangement>(
    id,
    agreementType,
    signedDate,
    fromDateTime,
    toDateTime,
    version,
    status,
    conditions,
    arrangements
), MultiTenant {

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