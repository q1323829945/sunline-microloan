package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.constant.APP_MICRO_LOAN_PUB_SUB
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublish
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanAgreement
import org.springframework.stereotype.Component

@Component
class LoanAgreementPublishImpl:LoanAgreementPublish {

    override fun loanAgreementPaid(applicationId: Long) {
        PubSubService.publish(
            pubSubName = APP_MICRO_LOAN_PUB_SUB,
            topic = LoanAgreementPublishTopic.LOAN_AGREEMENT_PAID.toString(),
            payload = applicationId,
            tenant = ContextUtil.getTenant()
        )
    }

    override fun loanAgreementSigned(applicationId: Long) {
        PubSubService.publish(
            pubSubName = APP_MICRO_LOAN_PUB_SUB,
            topic = LoanAgreementPublishTopic.LOAN_AGREEMENT_SIGNED.toString(),
            payload = applicationId,
            tenant = ContextUtil.getTenant()
        )
    }
}