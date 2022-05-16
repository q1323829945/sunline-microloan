package cn.sunline.saas.risk.control.datasource.factory

import cn.sunline.saas.risk.control.datasource.factory.impl.*
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.DataItem.*


object DataItemFactory{

    fun instance(dataItem: DataItem):DataItemCalculationInterface{
        return when(dataItem){
            CREDIT_RISK -> CreditRisk()
            CUSTOMER_CREDIT_RATE -> CustomerCreditRate()
            FRAUD_EVALUATION -> FraudEvaluation()
            REGULATORY_COMPLIANCE -> RegulatoryCompliance()
        }
    }
}