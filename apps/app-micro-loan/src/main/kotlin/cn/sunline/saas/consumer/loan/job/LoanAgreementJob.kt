package cn.sunline.saas.consumer.loan.job

import cn.sunline.saas.consumer.loan.job.actor.LoanAgreementActor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @title: LoanAgreementJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/11 16:39
 */
@RestController
@RequestMapping("/actors")
class LoanAgreementJob {

    @Autowired
    private lateinit var loanAgreementActor: LoanAgreementActor

    @PutMapping("/LoanAgreementActor/{actorId}/method/remind/autoPayment")
    fun autoPayment(@RequestParam actorId:Long) {
        loanAgreementActor.autoPayment(actorId)
    }

    @PutMapping("/LoanAgreementActor/{actorId}/method/remind/applyInterest")
    fun applyInterest(@RequestParam actorId:Long) {
        loanAgreementActor.applyInterest(actorId)
    }


}