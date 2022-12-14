package cn.sunline.saas.partner.integrated.model.db

import cn.sunline.saas.partner.integrated.model.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: IntegratedConfiguration
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 13:57
 */
@Entity
@Table(
    name = "partner_integrated",
)
class PartnerIntegrated(
    @Id
    val tenantId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "customer_credit_rating_partner",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    var customerCreditRatingPartner: CustomerCreditRatingPartner,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "credit_risk_partner", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var creditRiskPartner: CreditRiskPartner,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "regulatory_compliance_partner",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    var regulatoryCompliancePartner: RegulatoryCompliancePartner,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "fraud_evaluation_partner", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var fraudEvaluationPartner: FraudEvaluationPartner,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "disbursement_partner", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var disbursementPartner: DisbursementPartner,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "financial_accounting_partner",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    var financialAccountingPartner: FinancialAccountingPartner,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null
)
