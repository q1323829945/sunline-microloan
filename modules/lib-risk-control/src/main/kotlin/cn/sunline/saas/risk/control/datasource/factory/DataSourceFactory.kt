package cn.sunline.saas.risk.control.datasource.factory

import cn.sunline.saas.risk.control.datasource.factory.impl.Source1
import cn.sunline.saas.risk.control.datasource.factory.impl.Source2
import cn.sunline.saas.risk.control.datasource.factory.impl.Source3
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.DataItem.*


object DataSourceFactory{

    fun instance(dataItem: DataItem):DataSourceCalculationInterface{
        return when(dataItem){
            CREDIT_RISK -> Source1()
            CUSTOMER_CREDIT_RATE -> Source2()
            FRAUD_EVALUATION -> Source3()
            REGULATORY_COMPLIANCE -> Source1()
        }
    }
}