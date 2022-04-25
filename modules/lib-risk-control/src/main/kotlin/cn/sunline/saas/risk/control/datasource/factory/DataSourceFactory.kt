package cn.sunline.saas.risk.control.datasource.factory

import cn.sunline.saas.risk.control.datasource.factory.impl.Source1
import cn.sunline.saas.risk.control.datasource.factory.impl.Source2
import cn.sunline.saas.risk.control.datasource.factory.impl.Source3
import cn.sunline.saas.risk.control.rule.modules.DataSourceType
import cn.sunline.saas.risk.control.rule.modules.DataSourceType.*


object DataSourceFactory{

    fun instance(dataSourceType: DataSourceType):DataSourceCalculationInterface{
        return when(dataSourceType){
            SOURCE1 -> Source1()
            SOURCE2 -> Source2()
            SOURCE3 -> Source3()
        }
    }
}