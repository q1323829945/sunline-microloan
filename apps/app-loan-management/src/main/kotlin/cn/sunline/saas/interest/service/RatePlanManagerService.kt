package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.dto.DTORatePlanAdd
import cn.sunline.saas.interest.dto.DTORatePlanChange
import cn.sunline.saas.interest.dto.DTORatePlanView
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

interface RatePlanManagerService {

    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>

    fun getAll(type: RatePlanType, pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>

    fun addOne(dtoRatePlan: DTORatePlanAdd): ResponseEntity<DTOResponseSuccess<DTORatePlanView>>

    fun updateOne(id: Long,dtoRatePlan: DTORatePlanChange): ResponseEntity<DTOResponseSuccess<DTORatePlanView>>
}