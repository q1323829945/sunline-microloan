package cn.sunline.saas.invoice.controller

import cn.sunline.saas.invoice.service.dto.DTOInvoiceCalculateView
import cn.sunline.saas.invoice.service.dto.DTOInvoiceInfoView
import cn.sunline.saas.invoice.service.dto.DTOInvoiceRepay
import cn.sunline.saas.invoice.service.LoanInvoiceService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("invoice")
class LoanInvoiceController {

    @Autowired
    private lateinit var loanInvoiceService: LoanInvoiceService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping("/invoice/calculate/{invoiceId}")
    fun calculate(@PathVariable("invoiceId") invoiceId: Long): ResponseEntity<DTOResponseSuccess<DTOInvoiceCalculateView>> {
        val calculateView = loanInvoiceService.calculate(invoiceId)
        val response = objectMapper.convertValue<DTOInvoiceCalculateView>(calculateView)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping(" /invoice/retrieve/{customerId}/history/{invoiceStartDate}/{invoiceEndDate}")
    fun retrieveHistory(@PathVariable("customerId") customerId: Long,
                       @PathVariable("invoiceStartDate") invoiceStartDate: String,
                       @PathVariable("invoiceEndDate") invoiceEndDate: String,
                       pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {

        val page = loanInvoiceService.getHistoryPage(customerId,invoiceStartDate,invoiceEndDate,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @PostMapping("/invoice/repay")
    fun repay(@RequestBody dtoInvoiceRepay: DTOInvoiceRepay): ResponseEntity<DTOResponseSuccess<DTOInvoiceInfoView>> {
        val response = loanInvoiceService.repay(dtoInvoiceRepay)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("/invoice/retrieve/{customerId}/current")
    fun retrieveCurrent(@PathVariable customerId: Long): ResponseEntity<DTOResponseSuccess<DTOInvoiceInfoView>>{
        val response = loanInvoiceService.retrieveCurrent(customerId)
        return DTOResponseSuccess(response).response()
    }
}
