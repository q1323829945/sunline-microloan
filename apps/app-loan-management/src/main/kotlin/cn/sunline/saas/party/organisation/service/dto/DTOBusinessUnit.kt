package cn.sunline.saas.party.organisation.service.dto

import cn.sunline.saas.party.organisation.model.BusinessUnitType


data class DTOBusinessUnitPaged(
    val organisationId:String,
    val organisationName: String,
    val businessUnit: MutableList<DTOBusinessUnit>
)

data class DTOBusinessUnit(
    val id:String,
    val type:BusinessUnitType
)


