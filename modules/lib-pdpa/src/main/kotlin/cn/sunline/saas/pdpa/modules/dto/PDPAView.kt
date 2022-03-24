package cn.sunline.saas.pdpa.modules.dto

import cn.sunline.saas.global.constant.CountryType

data class PDPAView(
        val id:Long,
        val customerId:Long,
        val companyData:String,
        val personalData:String,
        val signature:String?,
        val countryType: CountryType,
)
