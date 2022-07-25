package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.rpc.pubsub.dto.DTODocumentGeneration
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationData

interface CustomerOfferPublish {
    fun initiateUnderwriting(dtoLoanApplicationData: DTOLoanApplicationData)

    fun registeredOrganisation()

    fun registeredPerson(dtoPersonAdd: DTOPersonAdd)

    fun documentGeneration(dtoDocumentGeneration: DTODocumentGeneration)
}