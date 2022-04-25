package cn.sunline.saas.risk.control.datasource.factory.impl

import cn.sunline.saas.risk.control.datasource.factory.DataSourceCalculationInterface

class Source3: DataSourceCalculationInterface {
    override fun calculation(customerId:Long): Int {
        return 999
    }
}