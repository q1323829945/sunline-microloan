package cn.sunline.saas.customer.offer.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType

data class DTOCustomerOfferView(
        val customerOfferProcedure: CustomerOfferProcedureView,
        var product: ProductView?
)


data class CustomerOfferProcedureView(
        val customerId: String,
        val customerOfferProcess: String,
        val employee: Long,
        var customerOfferId:String?,
        var customerOfferProcessNextTask:String?,
)

data class ProductView(
        var productId: String?,
        val amountConfiguration: DTOAmountConfigurationView?,
        val termConfiguration: DTOTermConfigurationView?,
)


data class DTOCustomerOfferLoanView (
        var customerOfferProcedure:DTOCustomerOfferProcedureView? = null,
        var pdpa:DTOPdpaView? = null,
        var product:DTOProductView? = null,
        val loan:DTOLoan? = null,
        val company:DTOCompany? = null,
        val contact:DTOContact? = null,
        val detail:DTODetail? = null,
        val guarantor:DTOGuarantor? = null,
        val financial:DTOFinancial? = null,
        val uploadDocument:List<DTOUploadDocument>? = null,
        val kyc:DTOKyc? = null,
        val referenceAccount:DTOReferenceAccount? = null
)

data class DTOCustomerOfferProcedureView(
        val customerId: Long,
        val customerOfferProcess: String,  //TODO
        val employee: Long,
        var customerOfferId:Long?,
        val customerOfferProcessNextTask:String?,  //TODO
        var status: ApplyStatus?
)


data class DTOProductView(
        var productId: Long,
        var identificationCode:String? = null,
        var name:String? = null,
        var version:String? = null,
        var description:String? = null,
        var amountConfiguration: DTOAmountConfigurationView? = null,
        var termConfiguration: DTOTermConfigurationView? = null,
)

data class DTOAmountConfigurationView(
        val maxValueRange: String?,
        val minValueRange: String?
)

data class DTOTermConfigurationView(
        val maxValueRange: LoanTermType?,
        val minValueRange: LoanTermType?
)


data class DTOPdpaView (
        val id:String,
        var pdpaInformation:List<DTOPdpaItemView>? = null
)

data class DTOPdpaItemView(
        val item:String,
        val information: List<DTOPdpaInformationView>
)

data class DTOPdpaInformationView(
        val label:String,
        val name:String
)
