package cn.sunline.saas.invoice.arrangement.service

import cn.sunline.saas.invoice.arrangement.model.InvoiceArrangement
import cn.sunline.saas.invoice.arrangement.repository.InvoiceArrangementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: InvoiceArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/18 14:53
 */
data class DTOInvoiceArrangement(
    val invoiceDay: Int?,
    val repaymentDay: Int?,
    val graceDays: Int?,
)

@Service
class InvoiceArrangementService(private val invoiceArrangementRepository: InvoiceArrangementRepository) :
    BaseMultiTenantRepoService<InvoiceArrangement, Long>(invoiceArrangementRepository) {

    fun registerInvoiceArrangement(agreementId: Long,dtoInvoiceArrangement: DTOInvoiceArrangement): InvoiceArrangement {
        // compute first invoice day and repayment day



        return save(
            InvoiceArrangement(
                agreementId,
                dtoInvoiceArrangement.invoiceDay,
                dtoInvoiceArrangement.repaymentDay,
                dtoInvoiceArrangement.graceDays
            )
        )
    }

}