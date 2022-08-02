package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.pubsub.StatisticsPublish
import cn.sunline.saas.rpc.pubsub.StatisticsPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOCommissionDetail
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationDetail
import org.springframework.stereotype.Component

@Component
class StatisticsPublishImpl : StatisticsPublish {

    override fun addLoanApplicationStatistics() {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = StatisticsPublishTopic.LOAN_APPLICATION_STATISTICS.toString(),
            payload = null,
            tenant = ContextUtil.getTenant()
        )
    }

    override fun addLoanApplicationDetail(dtoLoanApplicationDetail: DTOLoanApplicationDetail) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = StatisticsPublishTopic.LOAN_APPLICATION_DETAIL.toString(),
            payload = dtoLoanApplicationDetail,
            tenant = ContextUtil.getTenant()
        )
    }


    override fun addCommissionDetail(dtoCommissionDetail: DTOCommissionDetail) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = StatisticsPublishTopic.COMMISSION_DETAIL.toString(),
            payload = dtoCommissionDetail,
            tenant = ContextUtil.getTenant()
        )
    }

    override fun addCommissionStatistics() {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = StatisticsPublishTopic.COMMISSION_STATISTICS.toString(),
            payload = null,
            tenant = ContextUtil.getTenant()
        )
    }
}