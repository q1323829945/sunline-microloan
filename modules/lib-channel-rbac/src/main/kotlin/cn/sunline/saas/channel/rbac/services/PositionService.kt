package cn.sunline.saas.channel.rbac.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.global.constant.PositionType
import cn.sunline.saas.channel.rbac.exception.PositionChangeException
import cn.sunline.saas.channel.rbac.exception.PositionNotFoundException
import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionView
import cn.sunline.saas.channel.rbac.repositories.PositionRepository
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

@Service
class PositionService(
    val baseRepository: PositionRepository,
    val sequence: Sequence
) : BaseMultiTenantRepoService<Position, String>(baseRepository) {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    var defaultPositions:MutableList<String> = mutableListOf()

    init {
        PositionType.values().forEach {
            defaultPositions.add(it.position)
        }
    }

    fun addOne(dtoPositionAdd: DTOPositionAdd): DTOPositionView {
        val check = getByName(dtoPositionAdd.name)
        check?.run {
            throw PositionChangeException("position has been exist!")
        }
        val position = save(
            Position(
            id = getId(dtoPositionAdd.name),
            name = dtoPositionAdd.name,
            remark = dtoPositionAdd.remark
        )
        )

        return convertToDTOPositionView(position)
    }

    fun updateOne(oldOne: Position, newOne: Position): DTOPositionView {
        val check = getByName(newOne.name)
        if(check != null && oldOne.name != newOne.name){
            throw PositionChangeException("position has been exist!")
        }

        oldOne.name = newOne.name
        oldOne.remark = newOne.remark
        oldOne.users = newOne.users
        val position = save(oldOne)

        return convertToDTOPositionView(position)
    }

    fun paged(name: String?,pageable: Pageable):Page<DTOPositionView>{

        return getPageWithTenant({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            name?.run { predicates.add(criteriaBuilder.like(root.get("name"),"$name%")) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable).map {
            objectMapper.convertValue(it)
        }
    }

    private fun getId(name:String):String{
        return if(defaultPositions.contains(name)){
            name + ContextUtil.getTenant()
        } else {
            sequence.nextId().toString()
        }
    }

    fun getDetails(id:String): DTOPositionView {
        val position = getOne(id)?: throw PositionNotFoundException("Invalid position")

        return convertToDTOPositionView(position)
    }

    private fun convertToDTOPositionView(position: Position): DTOPositionView {
        return DTOPositionView(
            id = position.id,
            name = position.name,
            remark = position.remark,
            users =position.users?.run { objectMapper.convertValue(this) }
        )
    }

    private fun getByName(name:String): Position?{
        val position = getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("name"),name))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
        return position
    }
}