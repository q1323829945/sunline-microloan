package cn.sunline.saas.risk.control.datasource.factory.impl

import cn.sunline.saas.risk.control.datasource.factory.DataSourceCalculationInterface

class Source2: DataSourceCalculationInterface {
    override fun calculation(): Long {
        return 4
    }
}