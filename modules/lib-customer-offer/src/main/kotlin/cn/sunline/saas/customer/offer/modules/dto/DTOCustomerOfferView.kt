package cn.sunline.saas.customer.offer.modules.dto

data class DTOCustomerOfferView(
        val customerOfferProcedure: CustomerOfferProcedureView,
        var product: ProductView?
)
data class CustomerOfferProcedureView(
        val customerId: Long,
        val customerOfferProcess: String,
        val employee: Long,
        var customerOfferId:Long,
        var customerOfferProcessNextTask:String?,
)

data class ProductView(
        var productId: Long?,
        val amountConfiguration: DTOAmountConfiguration?,
        val termConfiguration: DTOTermConfiguration?,
)

