package cn.sunline.saas.pdpa.factory

import cn.sunline.saas.global.constant.CountryType
import cn.sunline.saas.global.constant.CountryType.*
import cn.sunline.saas.pdpa.factory.impl.ChinaPDPA
import org.springframework.stereotype.Component

@Component
class PDPAFactory {

    fun getInstance(countryType: CountryType):PDPAApi{

        return when(countryType){
            CHN -> ChinaPDPA()
        }
    }
}