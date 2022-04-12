package cn.sunline.saas.underwriting.controllers

import cn.sunline.saas.underwriting.model.db.UnderwritingApplicationData
import cn.sunline.saas.underwriting.service.UnderwritingService
import io.dapr.Topic
import io.dapr.client.domain.CloudEvent
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @title: UnderwritingController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 15:01
 */
@RestController
@RequestMapping("Underwriting")
class UnderwritingController(private val integratedConfigurationService: UnderwritingService) {

    @Topic(name = "submit", pubsubName = "underwriting-pub-sub")
    @PostMapping("/Initiate")
    fun initiate(@RequestBody(required = false) cloudEvent: CloudEvent<UnderwritingApplicationData>): Mono<Unit> {
        return Mono.fromRunnable {
            //TODO submit data convert to UnderwritingApplicationData

            integratedConfigurationService.initiate(cloudEvent.data)
        }
    }
}