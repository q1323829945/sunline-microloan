package cn.sunline.saas.partner.integrated.model.db

import cn.sunline.saas.partner.integrated.model.CreditRiskPartner
import cn.sunline.saas.partner.integrated.model.CustomerCreditRatingPartner
import cn.sunline.saas.partner.integrated.model.FraudEvaluationPartner
import cn.sunline.saas.partner.integrated.model.RegulatoryCompliancePartner
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
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

    @CreationTimestamp
    var created: Instant? = null,

    @UpdateTimestamp
    var updated: Instant? = null
)
