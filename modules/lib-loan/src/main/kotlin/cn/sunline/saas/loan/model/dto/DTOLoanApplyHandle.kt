package cn.sunline.saas.loan.model.dto

import java.util.Date

data class DTOLoanApplyHandle(
    val applicationId:String,
    val supplement:String? = null,
    val supplementDate:Date? = null
)
