package cn.sunline.saas.pdpa.services

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InitPdpa {

    @Autowired
    private lateinit var pdpaService:PdpaService

    @Autowired
    private lateinit var pdpaAuthorityService: PdpaAuthorityService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun addPdpaAuthority(){
        pdpaAuthorityService.register()
    }

    fun addPdpa(){
        val pdpa = pdpaService.getByCountryAndLanguage(CountryType.CHN,"zh-CN")
        pdpa?:run {
            val pdpaInformation = objectMapper.readValue<List<DTOPdpaItem>>(json)
            val dtoPdpa = DTOPdpaAdd(
                country = CountryType.CHN,
                language = "zh-CH",
                pdpaInformation = pdpaInformation
            )

            pdpaService.addOne(dtoPdpa)
        }

    }

    private val json =     """
      [
          {
              "item": "on behalf of",
              "information":
              [
                  {
                      "label": "company type",
                      "name": "company type"
                  },
                  {
                      "label": "Juristic name",
                      "name": "Juristic name"
                  },
                  {
                      "label": "Juristic ID",
                      "name": "Juristic ID"
                  },
                  {
                      "label": "Register Date",
                      "name": "Register Date"
                  }
              ]
          },
          {
              "item": "contact information",
              "information":
              [
                  {
                      "label": "Mr./Mrs./Ms./Otiher",
                      "name": "Mr./Mrs./Ms./Otiher"
                  },
                  {
                      "label": "Contact perpon",
                      "name": "Contact perpon"
                  },
                  {
                      "label": "Address",
                      "name": "Address"
                  },
                  {
                      "label": "Road",
                      "name": "Road"
                  },
                  {
                      "label": "Sub district",
                      "name": "Sub district"
                  },
                  {
                      "label": "District",
                      "name": "District"
                  },
                  {
                      "label": "Province",
                      "name": "Province"
                  },
                  {
                      "label": "Postal code",
                      "name": "Postal code"
                  },
                  {
                      "label": "Telphone No.",
                      "name": "Telphone No."
                  },
                  {
                      "label": "fax No",
                      "name": "fax No"
                  }
              ]
          },
          {
              "item": "Business information",
              "information":
              [
                  {
                      "label": "businesstype",
                      "name": "businesstype"
                  },
                  {
                      "label": "average revenue",
                      "name": "average revenue"
                  },
                  {
                      "label": "anerage revenue per year",
                      "name": "anerage revenue per year"
                  },
                  {
                      "label": "No.of Employees",
                      "name": "No.of Employees"
                  }
              ]
          },
          {
              "item": "Loan Details",
              "information":
              [
                  {
                      "label": "what is your purpose",
                      "name": "what is your purpose"
                  },
                  {
                      "label": "requlred loan amount",
                      "name": "requlred loan amount"
                  },
                  {
                      "label": "loan type",
                      "name": "loan type"
                  },
                  {
                      "label": "other required products/services",
                      "name": "other required products/services"
                  },
                  {
                      "label": "negative financial record",
                      "name": "negative financial record"
                  },
                  {
                      "label": "convenient time to call",
                      "name": "convenient time to call"
                  }
              ]
          },
          {
              "item": "Preferred Branch",
              "information":
              [
                  {
                      "label": "select province",
                      "name": "select province"
                  },
                  {
                      "label": "select branch",
                      "name": "select branch"
                  }
              ]
          }
      ]
    """
}