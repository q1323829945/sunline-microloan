package cn.sunline.saas.rpc.bindings

import cn.sunline.saas.rpc.bindings.dto.DTOChannelData

interface ChannelBindings {

    fun syncChannel(dtoChannelData: DTOChannelData)

}