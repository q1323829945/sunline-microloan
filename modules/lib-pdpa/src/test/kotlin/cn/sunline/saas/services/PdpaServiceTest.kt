package cn.sunline.saas.services

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaChange
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import cn.sunline.saas.pdpa.services.PdpaService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PdpaServiceTest {
    @Autowired
    private lateinit var pdpaService: PdpaService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private var id:Long? = null
    @BeforeAll
    fun init() {
        ContextUtil.setTenant("12344566")

        val pdpa = pdpaService.addOne(
            DTOPdpaAdd(
                country = CountryType.CHN,
                language = LanguageType.CHINESE,
                pdpaInformation = items()
            )
        )

        id = pdpa.id.toLong()
    }

    @Test
    fun `get one`(){
        val pdpa = pdpaService.getDTOPdpaView(id!!)

        Assertions.assertThat(pdpa.pdpaInformation).isNotNull
    }

    @Test
    fun `get paged`(){
        val paged = pdpaService.getPdpaPaged(null, Pageable.unpaged())

        Assertions.assertThat(paged.size).isEqualTo(1)
    }

    @Test
    fun `update one`(){
        val update = pdpaService.updateOne(
            id = id!!,
            dtoPdpaChange = DTOPdpaChange(items2())
        )

        Assertions.assertThat(update.pdpaInformation).isNotEqualTo(items())
        Assertions.assertThat(update.pdpaInformation).isEqualTo(items2())
    }

    @Test
    fun `get by country and language`(){
        val pdpa = pdpaService.getByCountryAndLanguage(CountryType.CHN,LanguageType.CHINESE)
        Assertions.assertThat(pdpa).isNotNull

    }

    private fun items():List<DTOPdpaItem>{
        val json = "        [\n" +
                "            {\n" +
                "                \"item\": \"business information\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"key\": \"name\",\n" +
                "                        \"name\": \"name\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"alias\",\n" +
                "                        \"name\": \"alias\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"name pinyin\",\n" +
                "                        \"name\": \"name pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"alias pinyin\",\n" +
                "                        \"name\": \"alias pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"gender\",\n" +
                "                        \"name\": \"gender\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"birth\",\n" +
                "                        \"name\": \"birth\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"internationgal\",\n" +
                "                        \"name\": \"internationgal\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"register address\",\n" +
                "                        \"name\": \"register address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"hdb type\",\n" +
                "                        \"name\": \"hdb type\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"notice\",\n" +
                "                        \"name\": \"notice\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"mobile phone\",\n" +
                "                        \"name\": \"mobile phone\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"email\",\n" +
                "                        \"name\": \"email\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"item\": \"perpon informationn\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"key\": \"outline\",\n" +
                "                        \"name\": \"outline\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"compangy\",\n" +
                "                        \"name\": \"compangy\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"uens\",\n" +
                "                        \"name\": \"uens\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"finance\",\n" +
                "                        \"name\": \"finance\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"capital\",\n" +
                "                        \"name\": \"capital\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"leader\",\n" +
                "                        \"name\": \"leader\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"shareholder\",\n" +
                "                        \"name\": \"shareholder\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]"
        return objectMapper.readValue(json)
    }


    private fun items2():List<DTOPdpaItem>{
        val json = "        [\n" +
                "            {\n" +
                "                \"item\": \"business information\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"key\": \"name\",\n" +
                "                        \"name\": \"name\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"alias\",\n" +
                "                        \"name\": \"alias\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"name pinyin\",\n" +
                "                        \"name\": \"name pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"alias pinyin\",\n" +
                "                        \"name\": \"alias pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"gender\",\n" +
                "                        \"name\": \"gender\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"birth\",\n" +
                "                        \"name\": \"birth\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"internationgal\",\n" +
                "                        \"name\": \"internationgal\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"register address\",\n" +
                "                        \"name\": \"register address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"hdb type\",\n" +
                "                        \"name\": \"hdb type\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"notice\",\n" +
                "                        \"name\": \"notice\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"mobile phone\",\n" +
                "                        \"name\": \"mobile phone\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"email\",\n" +
                "                        \"name\": \"email\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"item\": \"perpon informationn\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"key\": \"outline\",\n" +
                "                        \"name\": \"outline\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"compangy\",\n" +
                "                        \"name\": \"compangy\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"uens\",\n" +
                "                        \"name\": \"uens\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"finance\",\n" +
                "                        \"name\": \"finance\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"capital\",\n" +
                "                        \"name\": \"capital\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"leader\",\n" +
                "                        \"name\": \"leader\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]"
        return objectMapper.readValue(json)
    }
}