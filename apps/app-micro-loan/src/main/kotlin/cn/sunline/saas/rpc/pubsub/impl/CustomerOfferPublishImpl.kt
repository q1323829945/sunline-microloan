package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT_PUB_SUB
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.rpc.pubsub.CustomerOfferPublish
import cn.sunline.saas.rpc.pubsub.CustomerOfferPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTODocumentGeneration
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationData
import org.springframework.stereotype.Component

@Component
class CustomerOfferPublishImpl: CustomerOfferPublish {

    override fun initiateUnderwriting(dtoLoanApplicationData: DTOLoanApplicationData) {

        PubSubService.publish(
            APP_LOAN_MANAGEMENT_PUB_SUB,
            CustomerOfferPublishTopic.INITIATE_UNDERWRITING.toString(),
            dtoLoanApplicationData
        )
    }

    override fun registeredOrganisation() {
        //TODO:
    }

    override fun registeredPerson(dtoPersonAdd: DTOPersonAdd) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = CustomerOfferPublishTopic.INITIATE_PERSON.toString(),
            payload = dtoPersonAdd,
            tenant = ContextUtil.getTenant().toString()
        )
    }

    override fun documentGeneration(dtoDocumentGeneration: DTODocumentGeneration) {
        return
        //TODO:
        PubSubService.publish(
            APP_LOAN_MANAGEMENT_PUB_SUB,
            CustomerOfferPublishTopic.DOCUMENT_GENERATION.toString(),
            dtoDocumentGeneration
        )
    }

}