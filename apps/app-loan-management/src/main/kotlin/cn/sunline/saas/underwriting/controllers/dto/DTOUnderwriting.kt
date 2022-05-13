package cn.sunline.saas.underwriting.controllers.dto

import cn.sunline.saas.underwriting.db.OperationType


data class DTOUnderwriting (
    val id: String,
    val customerId: String,
    val applicationData: DTOLoanApplicationData,
    var customerCreditRate: String? = null,
    var creditRisk: String? = null,
    var fraudEvaluation: String? = null,
    var regulatoryCompliance: String? = null,
    val status:OperationType?
)

