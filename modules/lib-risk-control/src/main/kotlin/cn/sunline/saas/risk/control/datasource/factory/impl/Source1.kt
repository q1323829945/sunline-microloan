package cn.sunline.saas.risk.control.datasource.factory.impl

import cn.sunline.saas.risk.control.datasource.factory.DataSourceCalculationInterface
import java.math.BigDecimal

class Source1:DataSourceCalculationInterface {
    override fun calculation(customerId:Long): BigDecimal {
        return BigDecimal(5)
    }
}