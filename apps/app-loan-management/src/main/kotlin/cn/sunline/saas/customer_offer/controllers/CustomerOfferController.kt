package cn.sunline.saas.customer_offer.controllers

import cn.sunline.saas.customer_offer.service.AppCustomerOfferService
import cn.sunline.saas.customer_offer.service.dto.DTOInvokeCustomerOfferView
import cn.sunline.saas.customer_offer.service.dto.DTOManagementCustomerOfferView
import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.websocket.server.PathParam

@RestController
@RequestMapping("CustomerOffer")
class CustomerOfferController {

    data class DTOUpdateStatus(
        val id:String,
        val underwritingType: UnderwritingType,
    )


    @Autowired
    private lateinit var appCustomerOfferService: AppCustomerOfferService

    @GetMapping
    fun getPaged(@PathParam("customerId") customerId:String?,
                 @PathVariable("productId") productId:String?,
                 @PathVariable("productName") productName:String?,
                 pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = appCustomerOfferService.getPaged(customerId?.toLong(),productId?.toLong(),productName,pageable)

        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PostMapping("approval")
    fun approval(@RequestBody  id:String):ResponseEntity<DTOResponseSuccess<Unit>>{
        appCustomerOfferService.approval(id.toLong())

        return DTOResponseSuccess(Unit).response()
    }

    @PostMapping("rejected")
    fun rejected(@RequestBody  id:String):ResponseEntity<DTOResponseSuccess<Unit>>{
        appCustomerOfferService.rejected(id.toLong())
        return DTOResponseSuccess(Unit).response()
    }


    @GetMapping("{id}")
    fun getDetail(@PathVariable(name = "id")id:String):ResponseEntity<DTOResponseSuccess<DTOManagementCustomerOfferView>>{
        val response = appCustomerOfferService.getDetail(id.toLong())
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("download")
    fun download(@PathParam("path")path:String,response: HttpServletResponse){
        appCustomerOfferService.download(path, response)
    }

    @GetMapping("invoke/{id}")
    fun getCustomerOffer(@PathVariable(name = "id")id:String): DTOInvokeCustomerOfferView {
        return appCustomerOfferService.getInvokeCustomerOffer(id.toLong())
    }
}