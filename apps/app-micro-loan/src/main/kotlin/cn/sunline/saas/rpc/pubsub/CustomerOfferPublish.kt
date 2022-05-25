package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationData

interface CustomerOfferPublish {
    fun initiateUnderwriting(dtoLoanApplicationData: DTOLoanApplicationData)

    fun registeredOrganisation()
}