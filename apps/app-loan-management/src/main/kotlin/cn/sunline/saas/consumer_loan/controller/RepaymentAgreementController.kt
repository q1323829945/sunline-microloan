package cn.sunline.saas.consumer_loan.controller

import cn.sunline.saas.consumer_loan.service.RepaymentAgreementManagerService
import cn.sunline.saas.customer_offer.service.dto.DTOInvokeCustomerOfferView
import cn.sunline.saas.customer_offer.service.dto.DTOManagementCustomerOfferView
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.websocket.server.PathParam


@RestController
@RequestMapping("CustomerLoan")
class RepaymentAgreementController {

    @Autowired
     private lateinit var repaymentAgreementManagerService: RepaymentAgreementManagerService

    @GetMapping
    fun getPaged(@PathParam("customerId") customerId:String?,
                 pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = repaymentAgreementManagerService.getPaged(customerId?.toLong(),pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping
    fun getHistoryPaged(@PathParam("customerId") customerId:String,
                 pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = repaymentAgreementManagerService.getHistoryPaged(customerId?.toLong(),pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }


    @PostMapping("finish")
    fun finish(@RequestBody id:String): ResponseEntity<DTOResponseSuccess<Unit>> {
        repaymentAgreementManagerService.finish(id.toLong())
        return DTOResponseSuccess(Unit).response()
    }

//    @PostMapping("rejected")
//    fun rejected(@RequestBody id:String): ResponseEntity<DTOResponseSuccess<Unit>> {
//        repaymentAgreementManagerService.rejected(id.toLong())
//        return DTOResponseSuccess(Unit).response()
//    }
//
//
//    @GetMapping("{id}")
//    fun getDetail(@PathVariable(name = "id")id:String): ResponseEntity<DTOResponseSuccess<DTOManagementCustomerOfferView>> {
//        val response = repaymentAgreementManagerService.getDetail(id.toLong())
//        return DTOResponseSuccess(response).response()
//    }
//
//    @GetMapping("download")
//    fun download(@PathParam("path")path:String, response: HttpServletResponse){
//        repaymentAgreementManagerService.download(path, response)
//    }
//
//    @GetMapping("invoke/{id}")
//    fun getCustomerOffer(@PathVariable(name = "id")id:String): DTOInvokeCustomerOfferView {
//        return repaymentAgreementManagerService.getInvokeCustomerOffer(id.toLong())
//    }
}