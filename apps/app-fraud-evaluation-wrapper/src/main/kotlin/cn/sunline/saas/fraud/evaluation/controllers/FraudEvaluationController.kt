package cn.sunline.saas.fraud.evaluation.controllers

import cn.sunline.saas.fraud.evaluation.dto.DTOFraudEvaluation
import cn.sunline.saas.fraud.evaluation.service.FraudEvaluationService
import io.dapr.Topic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("FraudEvaluation")
class FraudEvaluationController {
    @Autowired
    private lateinit var fraudEvaluationService: FraudEvaluationService


    @Topic(name = "FRAUD_EVALUATION", pubsubName = "underwriting-pub-sub")
    @PostMapping
    fun getFraudEvaluation(@RequestBody(required = false) dtoFraudEvaluation: DTOFraudEvaluation): Mono<Unit> {
        //TODO:
        println(dtoFraudEvaluation)
        return Mono.fromRunnable(){
            fraudEvaluationService.getFraudEvaluation(dtoFraudEvaluation)
        }
    }
}