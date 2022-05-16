package cn.sunline.saas.risk.control.datasource.factory.impl

import cn.sunline.saas.risk.control.datasource.factory.DataItemCalculationInterface

class FraudEvaluation: DataItemCalculationInterface {
    override fun calculation(applicationId: Long): Number {
        return 1
    }
}