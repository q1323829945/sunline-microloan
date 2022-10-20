package cn.sunline.saas.modules.dto

import cn.sunline.saas.modules.enum.FormatType
import cn.sunline.saas.modules.enum.MethodType


data class DTOApi(
    val serverId:String,
    val api:String,
    val method: MethodType,
    val formatType: FormatType = FormatType.Json,
    val enabled:Boolean = true,
)

