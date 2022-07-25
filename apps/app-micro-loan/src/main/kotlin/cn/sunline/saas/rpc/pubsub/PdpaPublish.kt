package cn.sunline.saas.rpc.pubsub

interface PdpaPublish {
    fun customerPdpaConfirm(customerId:String)
    fun customerPdpaWithdraw(customerId:String)
}