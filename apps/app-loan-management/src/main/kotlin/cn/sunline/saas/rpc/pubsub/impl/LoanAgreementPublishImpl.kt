package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublish
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanAgreement
import org.springframework.stereotype.Component

@Component
class LoanAgreementPublishImpl:LoanAgreementPublish {
    override fun updateLoanAgreementStatus(dtoLoanAgreement: DTOLoanAgreement) {
        PubSubService.publish(
            pubSubName = "underwriting-pub-sub",
            topic = LoanAgreementPublishTopic.UPDATE_LOAN_AGREEMENT_STATUS.toString(),
            payload = dtoLoanAgreement,
            tenant = ContextUtil.getTenant()
        )
    }
}