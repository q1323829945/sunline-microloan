package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOBusinessDetail

interface PositionKeepingPublish {
    fun addBusinessDetail(dtoBusinessDetail: DTOBusinessDetail)
}