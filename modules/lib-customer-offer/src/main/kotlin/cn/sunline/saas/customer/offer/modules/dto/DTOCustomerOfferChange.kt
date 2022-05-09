package cn.sunline.saas.customer.offer.modules.dto


data class DTOCustomerOfferChange(
        val customerOfferProcedure: DTOCustomerOfferProcedureChange,
        val product: DTOProductChange,
        val pdpa: DTOPDPAChange
)
data class DTOCustomerOfferProcedureChange(
        val customerId: Long,
        val customerOfferProcess: String,
        val employee: Long,
)

data class DTOProductChange(val productId: Long,val productName: String)

data class DTOPDPAChange(
        val pdpaTemplateId: Long,
        var signature: String?,
)


data class DTOCustomerOfferLoanChange (
        val loan:DTOLoan?,
        val company:DTOCompany?,
        val contact:DTOContact?,
        val detail:DTODetail?,
        val guarantor:DTOGuarantor?,
        val financial:DTOFinancial?,
        val uploadDocument:MutableList<DTOUploadDocument>?,
        val kyc:DTOKyc?,
)

