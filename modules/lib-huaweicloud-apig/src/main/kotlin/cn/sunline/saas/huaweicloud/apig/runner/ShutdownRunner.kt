package cn.sunline.saas.huaweicloud.apig.runner

import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.dto.BatchPublishParams
import com.sun.org.slf4j.internal.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy

@Component
class ShutdownRunner {
    protected val logger = LoggerFactory.getLogger(ShutdownRunner::class.java)

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