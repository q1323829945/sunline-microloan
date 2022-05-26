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

data class DTOProductAdd(val productId: Long, var productName: String)

data class DTOPDPAAdd(
        val pdpaTemplateId: Long,
        var signature: String?,
)


data class DTOCustomerOfferLoanAdd (
        val loan:DTOLoan?,
        val company:DTOCompany?,
        val contact:DTOContact?,
        val detail:DTODetail?,
        val guarantor:DTOGuarantor?,
        val financial:DTOFinancial?,
        val uploadDocument:MutableList<DTOUploadDocument>?,
        val kyc:DTOKyc?,
        val referenceAccount:DTOReferenceAccount?
)



