package cn.sunline.saas.pdpa.services

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import cn.sunline.saas.pdpa.exception.PdpaAlreadyExistException
import cn.sunline.saas.pdpa.exception.PdpaNotFoundException
import cn.sunline.saas.pdpa.modules.db.Pdpa
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaChange
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaView
import cn.sunline.saas.pdpa.repositories.PdpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import javax.persistence.criteria.Predicate

@Service
class PdpaService(
    private val pdpaRepository: PdpaRepository,
    private val sequence: Sequence
): BaseMultiTenantRepoService<Pdpa, Long>(pdpaRepository) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var obsApi: ObsApi

    //TODO:之前不知道为什么要放在这里
    fun sign(customerId:Long,pdpaTemplateId: Long,fileName:String,inputStream: InputStream):String{
        val key = "$customerId/signature/$pdpaTemplateId/$fileName"
        obsApi.putObject(PutParams(key,inputStream))

        return key
    }

    fun addOne(dtoPdpaAdd: DTOPdpaAdd): DTOPdpaView {
        getByCountryAndLanguage(
            dtoPdpaAdd.country,
            dtoPdpaAdd.language
        )?.run { throw PdpaAlreadyExistException("pdpa already exist!") }
        val id = sequence.nextId()
        val path = uploadPdpaInformation(dtoPdpaAdd.country, dtoPdpaAdd.language, dtoPdpaAdd.pdpaInformation)
        val pdpa = save(
            Pdpa(
                id = id,
                country = dtoPdpaAdd.country,
                language = dtoPdpaAdd.language,
                informationFilePath = path
            )
        )

        val dtoPdpaView = objectMapper.convertValue<DTOPdpaView>(pdpa)
        dtoPdpaView.pdpaInformation = dtoPdpaAdd.pdpaInformation
        return dtoPdpaView
    }

    fun updateOne(id:Long,dtoPdpaChange: DTOPdpaChange):DTOPdpaView{
        val oldOne = getOne(id)?: throw PdpaNotFoundException("Invalid pdpa")
        oldOne.informationFilePath = uploadPdpaInformation(oldOne.country, oldOne.language, dtoPdpaChange.pdpaInformation)

        val pdpa = save(oldOne)
        val dtoPdpaView = objectMapper.convertValue<DTOPdpaView>(pdpa)
        dtoPdpaView.pdpaInformation = dtoPdpaChange.pdpaInformation
        return dtoPdpaView
    }

    fun getPdpaPaged(country: CountryType?,pageable: Pageable):Page<Pdpa>{
        val newPageable = if(pageable.isUnpaged){
            Pageable.unpaged()
        } else {
            PageRequest.of(pageable.pageNumber,pageable.pageSize, Sort.by(Sort.Order.desc("created")))
        }

        return getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            country?.run { predicates.add(criteriaBuilder.equal(root.get<CountryType>("country"),country)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, newPageable)
    }

    fun getByCountryAndLanguage(country:CountryType,language: LanguageType):Pdpa?{
        return getOneWithTenant{root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<CountryType>("country"),country))
            predicates.add(criteriaBuilder.equal(root.get<LanguageType>("language"),language))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun getDTOPdpaView(id:Long):DTOPdpaView{
        val pdpa = getOne(id)?: throw PdpaNotFoundException("Invalid pdpa")

        return DTOPdpaView(
            id = pdpa.id.toString(),
            country = pdpa.country,
            language = pdpa.language,
            pdpaInformation = downloadPdpaInformation(pdpa.informationFilePath)
        )
    }

    private fun uploadPdpaInformation(country: CountryType,language: LanguageType,pdpaInformation:List<DTOPdpaItem>):String{
        val dateTime = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss"))
        val path = "pdpa/${ContextUtil.getTenant()}/$country/$language/$dateTime.json"

        val informationJson = objectMapper.valueToTree<JsonNode>(pdpaInformation).toPrettyString()
        obsApi.putObject(PutParams(path,informationJson))

        return path
    }

    private fun downloadPdpaInformation(path:String):List<DTOPdpaItem>{
        val json = when(val result = obsApi.getObject(GetParams(path))){
            is String -> result
            is InputStream -> String(result.readAllBytes())
            else -> throw PdpaNotFoundException("pdpa information not found")
        }
        return objectMapper.readValue(json)
    }
}

