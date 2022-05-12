package cn.sunline.saas.underwriting.event.dto

import cn.sunline.saas.underwriting.db.OperationType

data class DTOCustomerOffer (
    val id:Long,
    val operationType: OperationType,
)