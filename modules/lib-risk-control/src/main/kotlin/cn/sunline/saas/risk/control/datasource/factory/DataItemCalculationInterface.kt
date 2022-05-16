package cn.sunline.saas.risk.control.datasource.factory

interface DataItemCalculationInterface {
    fun calculation(applicationId:Long):Number
}