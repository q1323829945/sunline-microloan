package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.exception.RepaymentAgreementBusinessException
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoicePage
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer_offer.exceptions.CustomerOfferStatusException
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublish
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class RepaymentAgreementManagerService(
    val tenantDateTime: TenantDateTime,
    val loanAgreementPublish: LoanAgreementPublish,
    val customerOfferInvoke: CustomerOfferInvoke
) {
    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getPaged(customerId:Long?,pageable: Pageable): Page<DTOInvoicePage> {

        val page = invoiceService.getInvokesPaged(customerId,InvoiceStatus.ACCOUNTED,pageable).map{

            val loanAgreement = customerOfferInvoke.getLoanAgreementInfo(it.agreementId)

            DTOInvoicePage(
                id = it.id.toString(),
                agreementId = it.agreementId.toString(),
                invoiceType = it.invoiceType,
                fromDateTime = tenantDateTime.toTenantDateTime(it.invoicePeriodFromDate).toString(),
                toDateTime = tenantDateTime.toTenantDateTime(it.invoicePeriodToDate).toString(),
                invoiceStatus = it.invoiceStatus,
                amount = it.invoiceAmount.toPlainString(),
                currency = loanAgreement?.currency,
                userId = it.invoicee.toString(),
                repaymentStatus = it.repaymentStatus
            )
        }
        return page
    }

    fun getHistoryPaged(customerId:Long,pageable: Pageable): Page<DTOInvoicePage> {

        val page = invoiceService.getInvokesPaged(customerId,null,pageable).map{

            val loanAgreement = customerOfferInvoke.getLoanAgreementInfo(it.agreementId)

            DTOInvoicePage(
                id = it.id.toString(),
                agreementId = it.agreementId.toString(),
                invoiceType = it.invoiceType,
                fromDateTime = tenantDateTime.toTenantDateTime(it.invoicePeriodFromDate).toString(),
                toDateTime = tenantDateTime.toTenantDateTime(it.invoicePeriodToDate).toString(),
                invoiceStatus = it.invoiceStatus,
                amount = it.invoiceAmount.toPlainString(),
                currency = loanAgreement?.currency,
                userId = it.invoicee.toString(),
                repaymentStatus = it.repaymentStatus
            )
        }
        return page
    }

    fun finish(id: Long){
        updateStatus(id, InvoiceStatus.FINISHED)
    }

//    fun rejected(id:Long){
//        updateStatus(id, ApplyStatus.REJECTED)
//    }

    private fun updateStatus(id:Long,status: InvoiceStatus){
        val invoice = invoiceService.getOne(id)?: throw RepaymentAgreementBusinessException("Loan Invoice Not Found",ManagementExceptionCode.INVOICE_NOT_FOUND)
        checkStatus(invoice.invoiceStatus,status)
        invoice.invoiceStatus = status
        invoiceService.save(invoice)
    }

    private fun checkStatus(oldStatus: InvoiceStatus, newStatus: InvoiceStatus){
        when(oldStatus){
            InvoiceStatus.ACCOUNTED ->
                if(newStatus != InvoiceStatus.FINISHED)
                    throw CustomerOfferStatusException("status can not be update", ManagementExceptionCode.INVOICE_STATUS_ERROR)
            else -> throw RepaymentAgreementBusinessException("status can not be update", ManagementExceptionCode.INVOICE_STATUS_ERROR)
        }
    }
}