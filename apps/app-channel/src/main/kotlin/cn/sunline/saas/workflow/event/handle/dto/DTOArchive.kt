package cn.sunline.saas.workflow.event.handle.dto

import cn.sunline.saas.global.constant.ProductType

data class DTOArchive (
    val productType: ProductType,
    val data: Any
)