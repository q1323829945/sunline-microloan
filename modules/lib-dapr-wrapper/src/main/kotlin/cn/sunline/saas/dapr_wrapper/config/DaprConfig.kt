package cn.sunline.saas.dapr_wrapper.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * @title: DaprConfig
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 10:07
 */
@Component
@ConfigurationProperties(prefix = "dapr")
class DaprConfig(
    var port: Int = 3500,
    var actorIdleTimeout:String = "1h",
    var actorScanInterval:String = "30s",
    var drainOngoingCallTimeout:String = "30s",
    var drainRebalancedActors:Boolean = true,
    var reentrancyEnabled:Boolean = true,
    var reentrancyMaxStackDepth:Int = 32,
)

