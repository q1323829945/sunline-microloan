package cn.sunline.saas.customer.offer.modules.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.pdpa.modules.dto.PDPAInformationView
import java.math.BigDecimal


data class DTOCustomerOfferLoanView (
        val customerOfferProcedure:DTOCustomerOfferProcedureView,
        val pdpa:PDPAInformationView?,
        var product:DTOProductView,
        val loan:DTOLoan,
        val company:DTOCompany,
        val contact:DTOContact,
        val detail:DTODetail,
        val guarantor:DTOGuarantor,
        val financial:DTOFinancial,
        val uploadDocument:List<DTOUploadDocument>,
        val kyc:DTOKyc,
)

data class DTOCustomerOfferProcedureView(
        val customerId: Long,
        val customerOfferProcess: String,  //TODO
        val employee: Long,
        var customerOfferId:Long?,
        val customerOfferProcessNextTask:String?,  //TODO
        var status:ApplyStatus?
)


data class DTOProductView(
        var productId: Long,
        var identificationCode:String? = null,
        var name:String? = null,
        var version:String? = null,
        var description:String? = null,
        var amountConfiguration: DTOAmountConfiguration? = null,
        var termConfiguration: DTOTermConfiguration? = null,
)

data class DTOAmountConfiguration(
        val maxValueRange: BigDecimal?,
        val minValueRange: BigDecimal?
)

data class DTOTermConfiguration(
        val maxValueRange: LoanTermType?,
        val minValueRange: LoanTermType?
)
