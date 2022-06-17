package cn.sunline.saas.invoice.controller

import cn.sunline.saas.invoice.model.dto.DTOInvoiceInfoView
import cn.sunline.saas.invoice.model.dto.DTOInvoiceTrailView
import cn.sunline.saas.invoice.service.LoanInvoiceService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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

    @GetMapping("/retrieve/{customerId}/history/{invoiceStartDate}/{invoiceEndDate}")
    fun retrieveHistory(@PathVariable("customerId") customerId: Long,
                       @PathVariable("invoiceStartDate") invoiceStartDate: String?,
                       @PathVariable("invoiceEndDate") invoiceEndDate: String?
    ): ResponseEntity<DTOResponseSuccess<MutableList<DTOInvoiceInfoView>>> {
        val response = loanInvoiceService.getHistoryPage(customerId,invoiceStartDate,invoiceEndDate)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("/retrieve/{customerId}/current")
    fun retrieveCurrent(@PathVariable customerId: Long): ResponseEntity<DTOResponseSuccess<List<DTOInvoiceInfoView>>>{
        val response = loanInvoiceService.retrieveCurrentAccountedInvoices(customerId)
        return DTOResponseSuccess(response).response()
    }

}

