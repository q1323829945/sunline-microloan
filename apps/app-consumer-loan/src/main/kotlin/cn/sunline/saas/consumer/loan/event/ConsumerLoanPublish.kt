package cn.sunline.saas.consumer.loan.event

/**
 * @title: ConsumerLoanPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:08
 */
interface ConsumerLoanPublish {

    fun underwriting()

    fun financialAccounting()

    fun disbursement()
}