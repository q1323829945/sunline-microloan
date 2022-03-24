package cn.sunline.saas.pdpa.modules.dto

import cn.sunline.saas.global.constant.CountryType

data class PDPAInfo(
        val customerId:Long,
        val companyData:String,
        val personalData:String,
        val countryType: CountryType,
)
