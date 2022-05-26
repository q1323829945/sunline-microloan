package cn.sunline.saas.customer.offer.modules.dto


data class DTOCustomerOfferData(
        val customerOfferProcedure: DTOCustomerOfferProcedureData,
        val product: DTOProductData,
        val pdpa: DTOPDPAData
)


data class DTOCustomerOfferProcedureData(
        val customerId: Long,
        val customerOfferProcess: String,
        val employee: Long,
)

data class DTOProductData(val productId: Long,val productName: String)

data class DTOPDPAData(
        val pdpaTemplateId: Long,
        var signature: String?,
)


data class DTOCustomerOfferLoanData (
        val loan:DTOLoan?,
        val company:DTOCompany?,
        val contact:DTOContact?,
        val detail:DTODetail?,
        val guarantor:DTOGuarantor?,
        val financial:DTOFinancial?,
        val uploadDocument:MutableList<DTOUploadDocument>?,
        val kyc:DTOKyc?,
)