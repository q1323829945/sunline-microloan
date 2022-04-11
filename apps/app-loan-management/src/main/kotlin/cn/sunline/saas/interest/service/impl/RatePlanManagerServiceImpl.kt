package cn.sunline.saas.interest.service.impl

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.interest.dto.DTORatePlanAdd
import cn.sunline.saas.interest.dto.DTORatePlanChange
import cn.sunline.saas.interest.dto.DTORatePlanView
import cn.sunline.saas.interest.exception.RatePlanNotFoundException
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanManagerService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate


@Service
class RatePlanManagerServiceImpl: RatePlanManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    override fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = ratePlanService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORatePlanView>(it) }).response()
    }

    override fun getAll(type: RatePlanType, pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = ratePlanService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<RatePlanType>("type"), type))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORatePlanView>(it) }).response()
    }

    override fun addOne(dtoRatePlan: DTORatePlanAdd): ResponseEntity<DTOResponseSuccess<DTORatePlanView>> {
        val ratePlan = objectMapper.convertValue<RatePlan>(dtoRatePlan)
        val typeRatePlan = ratePlanService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("type"), ratePlan.type))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()
        if(typeRatePlan != null){
            throw ManagementException(ManagementExceptionCode.RATE_PLAN_TYPE_EXIST)
        }
        val savedRatePlan = ratePlanService.addOne(ratePlan)
        val responseRatePlan = objectMapper.convertValue<DTORatePlanView>(savedRatePlan)
        return DTOResponseSuccess(responseRatePlan).response()
    }

    override fun updateOne(id: Long,dtoRatePlan: DTORatePlanChange): ResponseEntity<DTOResponseSuccess<DTORatePlanView>> {
        val oldRatePlan = ratePlanService.getOne(id)?: throw RatePlanNotFoundException("Invalid ratePlan",
            ManagementExceptionCode.DATA_NOT_FOUND)
        val newRatePlan = objectMapper.convertValue<RatePlan>(dtoRatePlan)
        val savedRatePlan = ratePlanService.updateOne(oldRatePlan, newRatePlan)
        val responseRatePlan = objectMapper.convertValue<DTORatePlanView>(savedRatePlan)
        return DTOResponseSuccess(responseRatePlan).response()
    }

}