package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOCustomerDetail

interface PartyPublish {
    fun addCustomerDetail(dtoCustomerDetail: DTOCustomerDetail)
}