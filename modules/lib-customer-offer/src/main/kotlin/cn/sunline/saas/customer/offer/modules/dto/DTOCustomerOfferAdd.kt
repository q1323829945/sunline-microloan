package cn.sunline.saas.customer.offer.modules.dto

data class DTOCustomerOfferAdd(
        val customerOfferProcedure: DTOCustomerOfferProcedureAdd,
        val product: DTOProductAdd,
        val pdpa: DTOPDPAAdd
)
data class DTOCustomerOfferProcedureAdd(
        val customerId: Long,
        val customerOfferProcess: String,
        val employee: Long,
)

data class DTOProductAdd(val productId: Long)

data class DTOPDPAAdd(
        val pdpaTemplateId: Long,
        var signature: String,
)
