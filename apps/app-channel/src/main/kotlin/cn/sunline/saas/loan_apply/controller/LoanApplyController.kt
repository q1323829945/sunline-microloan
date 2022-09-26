package cn.sunline.saas.loan_apply.controller

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.loan.model.dto.DTOLoanAgent
import cn.sunline.saas.loan.model.dto.DTOLoanApplyStatus
import cn.sunline.saas.channel.product.model.dto.DTOProductAppView
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.loan_apply.service.LoanApplyAppService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.scheduler.ActorType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
@RequestMapping("loan")
class LoanApplyController {
    @Autowired
    private lateinit var loanApplyService: LoanApplyService

    @Autowired
    private lateinit var loanAgentService: LoanAgentService

    @Autowired
    private lateinit var loanApplyAppService: LoanApplyAppService

    data class DTOSubmitCallBack(
        val applicationId: String,
        val status:ApplyStatus,
    )

    data class DTOApplicationId(
        val applicationId: String
    )

    data class DTOLoanApplyChange(
        val productType: ProductType,
        val data:Any
    )

    data class DTOLoanApplySupplement(
        val applicationId:String,
        val username:String
    )

    data class DTOLoanApplyProduct(
        val applicationId: String,
        val productId:String,
    )

    @GetMapping("product/{productType}")
    fun getProduct(@PathVariable productType: ProductType):ResponseEntity<DTOResponseSuccess<DTOProductAppView>>{
        val product = loanApplyAppService.getProduct(productType)
        return DTOResponseSuccess(product).response()
    }

    @PostMapping("record")
    fun record(@RequestBody data:String):ResponseEntity<DTOResponseSuccess<DTOApplicationId>>{
        val loanBaseInformation = loanApplyAppService.loanRecord(data)
        return DTOResponseSuccess(DTOApplicationId(loanBaseInformation.applicationId.toString())).response()
    }

    @GetMapping
    fun paged(@PathParam(value = "productType")productType: ProductType?,
              @PathParam(value = "status")status:ApplyStatus?,
              pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanApplyAppService.getPaged(productType,status,null,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("supplement")
    fun getPagedBySupplement(@PathParam(value = "productType")productType: ProductType?,
              @PathParam(value = "status")status:ApplyStatus?,
              pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanApplyAppService.getPaged(productType,status,ContextUtil.getUserId(),pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("status/{applicationId}")
    fun getStatus(@PathVariable applicationId:String):ResponseEntity<DTOResponseSuccess<DTOLoanApplyStatus>>{
        val loanApply = loanAgentService.getStatus(applicationId.toLong())
        return DTOResponseSuccess(loanApply).response()
    }

    @GetMapping("{applicationId}")
    fun getOne(@PathVariable applicationId:String):ResponseEntity<DTOResponseSuccess<Any>>{
        val loanApply = loanApplyService.getLoanApplyDetails(applicationId.toLong())
        return DTOResponseSuccess(loanApply).response()
    }

    @PutMapping("update")
    fun update(@RequestBody dtoLoanApplyChange: DTOLoanApplyChange):ResponseEntity<DTOResponseSuccess<DTOApplicationId>>{
        val loanApply = loanApplyAppService.updateLoanApply(dtoLoanApplyChange.productType,dtoLoanApplyChange.data)
        return DTOResponseSuccess(DTOApplicationId(loanApply.applicationId.toString())).response()
    }

    @PutMapping("submit")
    fun submit(@RequestBody applicationId:DTOApplicationId):ResponseEntity<DTOResponseSuccess<DTOApplicationId>>{
        val loanApply = loanApplyAppService.loanApplySubmit(applicationId.applicationId)
        return DTOResponseSuccess(DTOApplicationId(loanApply.applicationId.toString())).response()
    }

    @PutMapping("submit/callback")
    fun submitCallback(@RequestBody dtoSubmitCallBack: DTOSubmitCallBack):ResponseEntity<DTOResponseSuccess<DTOApplicationId>>{
        val loanApply = loanApplyAppService.loanApplySubmitCallBack(dtoSubmitCallBack.applicationId,dtoSubmitCallBack.status)
        return DTOResponseSuccess(DTOApplicationId(loanApply.applicationId.toString())).response()
    }

    @PutMapping("approval")
    fun approval(@RequestBody applicationId:DTOApplicationId):ResponseEntity<DTOResponseSuccess<DTOApplicationId>>{
        val loanApply = loanApplyAppService.updateStatus(applicationId.applicationId,ApplyStatus.APPROVALED)
        return DTOResponseSuccess(DTOApplicationId(loanApply.applicationId.toString())).response()
    }

    @PutMapping("supplement")
    fun supplement(@RequestBody dtoLoanApplySupplement: DTOLoanApplySupplement):ResponseEntity<DTOResponseSuccess<Unit>>{
        loanApplyAppService.supplement(dtoLoanApplySupplement.applicationId,dtoLoanApplySupplement.username)
        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("product/set")
    fun setProduct(@RequestBody dtoLoanApplyProduct: DTOLoanApplyProduct):ResponseEntity<DTOResponseSuccess<Unit>>{
        val loanApply = loanApplyAppService.addProduct(dtoLoanApplyProduct.applicationId,dtoLoanApplyProduct.productId)
        return DTOResponseSuccess(Unit).response()
    }

    @GetMapping("agent/{applicationId}")
    fun getLoanAgentDetail(@PathVariable applicationId:String):ResponseEntity<DTOResponseSuccess<DTOLoanAgent>>{
        val agent = loanApplyAppService.getLoanAgentDetail(applicationId)
        return DTOResponseSuccess(agent).response()
    }

    @GetMapping("test1")
    fun test1(){
        loanApplyAppService.test1()
    }
    @GetMapping("test2")
    fun test2(@PathParam(value = "actorId")actorId: String,
              @PathParam(value = "jobId")jobId: String){
        ActorReminderService.deleteReminders(ActorType.LOAN_APPLY_STATISTICS.name,actorId,jobId)
    }
}