package cn.sunline.saas.partner.integrated.model.dto

import cn.sunline.saas.partner.integrated.model.CreditRiskPartner
import cn.sunline.saas.partner.integrated.model.CustomerCreditRatingPartner
import cn.sunline.saas.partner.integrated.model.FraudEvaluationPartner
import cn.sunline.saas.partner.integrated.model.RegulatoryCompliancePartner

data class DTOPartnerIntegrated(
    val id: Long,
    val customerCreditRatingPartner: CustomerCreditRatingPartner,
    val creditRiskPartner: CreditRiskPartner,
    val regulatoryCompliancePartner: RegulatoryCompliancePartner,
    val fraudEvaluationPartner: FraudEvaluationPartner
)