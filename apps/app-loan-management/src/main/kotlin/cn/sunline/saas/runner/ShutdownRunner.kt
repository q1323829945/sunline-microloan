package cn.sunline.saas.runner

import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.dto.BatchPublishParams
import cn.sunline.saas.huaweicloud.apig.constant.ApiResponsePage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy

@Component
class ShutdownRunner {

    @Autowired
    private lateinit var apiGatewayRunner:ApiGatewayRunner

    @PreDestroy
    fun apiOffLine(){
        println("offline api ...")
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

        println("offline api finish")
    }
}