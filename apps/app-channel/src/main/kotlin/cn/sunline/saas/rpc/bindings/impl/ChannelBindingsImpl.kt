package cn.sunline.saas.rpc.bindings.impl

import cn.sunline.saas.dapr_wrapper.bindings.BindingsService
import cn.sunline.saas.dapr_wrapper.constant.CHANNEL_SYNC_BINDINGS
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.bindings.BindingsOperation
import cn.sunline.saas.rpc.bindings.ChannelBindings
import cn.sunline.saas.rpc.bindings.dto.DTOChannelData
import org.springframework.stereotype.Component

@Component
class ChannelBindingsImpl: ChannelBindings {
    override fun syncChannel(dtoChannelData: DTOChannelData) {
        BindingsService.bindings(
            bindingsName = CHANNEL_SYNC_BINDINGS,
            operation = BindingsOperation.CREATE.name.lowercase(),
            payload = dtoChannelData,
            tenant = ContextUtil.getTenant()
        )
    }
}