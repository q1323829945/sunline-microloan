package cn.sunline.saas.risk.control.datasource.factory

interface DataSourceCalculationInterface {
    fun calculation(customerId:Long):Number
}