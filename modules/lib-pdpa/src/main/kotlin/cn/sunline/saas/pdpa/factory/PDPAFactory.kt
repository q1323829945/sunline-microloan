package cn.sunline.saas.pdpa.factory

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CountryType.*
import cn.sunline.saas.pdpa.factory.impl.ChinaPDPA
import org.springframework.stereotype.Component

@Component
class PDPAFactory {

    fun getInstance(countryType: CountryType):PDPAApi{

        return when(countryType){
            CHN -> ChinaPDPA()
            HKG -> TODO()
            MAC -> TODO()
            TWN -> TODO()
            SGP -> TODO()
            THA -> TODO()
            IND -> TODO()
            JPN -> TODO()
            KOR -> TODO()
            PAK -> TODO()
            USA -> TODO()
            GBR -> TODO()
            FRA -> TODO()
            DEU -> TODO()
            IDN -> TODO()
            MYS -> TODO()
        }
    }
}