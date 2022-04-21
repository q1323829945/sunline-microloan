package cn.sunline.saas.huaweicloud.apig.runner

import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.dto.BatchPublishParams
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy

@Component
class ShutdownRunner {
    private val logger: Logger = LoggerFactory.getLogger(ShutdownRunner::class.java)

    @Autowired
    private lateinit var apiGatewayRunner: ApiGatewayRunner

    @PreDestroy
    fun apiOffLine(){
        logger.debug("offline api ...")
        val list = apiGatewayRunner.getApiList(apiGatewayRunner.groupId!!)

        val idList = list.map {
            it.id
        }

        apiGatewayRunner.huaweiCloudApigApiService.batchPublish(
            BatchPublishParams(
                ActionType.offline,
                idList,
                apiGatewayRunner.environmentId!!
            )
        )

        logger.debug("offline api finish")
    }
}