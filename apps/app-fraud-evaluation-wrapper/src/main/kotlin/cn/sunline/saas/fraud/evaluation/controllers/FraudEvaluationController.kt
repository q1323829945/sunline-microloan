package cn.sunline.saas.fraud.evaluation.controllers

import cn.sunline.saas.fraud.evaluation.dto.DTOFraudEvaluation
import cn.sunline.saas.fraud.evaluation.service.FraudEvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("FraudEvaluation")
class FraudEvaluationController {
    @Autowired
    private lateinit var fraudEvaluationService: FraudEvaluationService

    @PostMapping
    fun getFraudEvaluation(@RequestBody(required = false) dtoFraudEvaluation: DTOFraudEvaluation): Mono<Unit> {
        //TODO:
        return Mono.fromRunnable(){
            fraudEvaluationService.getFraudEvaluation(dtoFraudEvaluation)
        }
    }
}