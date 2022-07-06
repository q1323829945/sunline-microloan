package cn.sunline.saas.invoice.controller

import cn.sunline.saas.invoice.model.dto.DTOInvoiceInfoView
import cn.sunline.saas.invoice.model.dto.DTOInvoiceScheduleView
import cn.sunline.saas.invoice.model.dto.DTOInvoiceTrailView
import cn.sunline.saas.invoice.service.LoanInvoiceService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("invoice")
class LoanInvoiceController {

    @Autowired
    private lateinit var loanInvoiceService: LoanInvoiceService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping("/calculate/{invoiceId}")
    fun calculate(@PathVariable("invoiceId") invoiceId: Long): ResponseEntity<DTOResponseSuccess<DTOInvoiceTrailView>> {
        val calculateView = loanInvoiceService.calculate(invoiceId)
        val response = objectMapper.convertValue<DTOInvoiceTrailView>(calculateView)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping(value = [
        "/retrieve/{customerId}/history/{invoiceStartDate}/{invoiceEndDate}",
        "/retrieve/{customerId}/history",
        "/retrieve/{customerId}/history/{invoiceStartDate}"
    ])
    fun retrieveHistory(@PathVariable("customerId",) customerId: Long,
                       @PathVariable("invoiceStartDate",required = false) invoiceStartDate: String?,
                       @PathVariable("invoiceEndDate",required = false) invoiceEndDate: String?
    ): ResponseEntity<DTOResponseSuccess<MutableList<DTOInvoiceInfoView>>> {
        val response = loanInvoiceService.getHistoryPage(customerId,invoiceStartDate,invoiceEndDate)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("/retrieve/{customerId}/current")
    fun retrieveCurrent(@PathVariable customerId: Long): ResponseEntity<DTOResponseSuccess<List<DTOInvoiceInfoView>>>{
        val response = loanInvoiceService.retrieveCurrentAccountedInvoices(customerId)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("/schedule/{agreementId}/retrieve")
    fun getInstalmentSchedule(@PathVariable agreementId: Long): ResponseEntity<DTOResponseSuccess<DTOInvoiceScheduleView>>{
        val response = loanInvoiceService.getInstalmentSchedule(agreementId)
        return DTOResponseSuccess(response).response()
    }

}
