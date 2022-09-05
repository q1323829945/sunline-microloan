package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOChannelData

interface ChannelPublish {

    fun syncChannel(dtoChannelData: DTOChannelData)

}