package cn.sunline.saas.rbac.service

import cn.sunline.saas.channel.rbac.exception.PositionChangeException
import cn.sunline.saas.channel.rbac.exception.PositionNotFoundException
import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionChange
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionView
import cn.sunline.saas.channel.rbac.services.PositionService
import cn.sunline.saas.channel.rbac.services.UserService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PositionManagerService {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var positionService: PositionService

    @Autowired
    private lateinit var userService: UserService

    fun updateOne(dtoPositionChange: DTOPositionChange):DTOPositionView{
        val oldOne = positionService.getOne(dtoPositionChange.id)?: throw PositionNotFoundException("Invalid position")
        if(positionService.defaultPositions.contains(oldOne.name)
            && oldOne.name != dtoPositionChange.name){
            throw PositionChangeException("default position can't change name")
        }
        val newOne = objectMapper.convertValue<Position>(dtoPositionChange)
        val users = mutableListOf<User>()
        dtoPositionChange.usersList?.forEach {
            val user = userService.getOne(it.toLong())
            user?.run {
                users.add(this)
            }
        }
        newOne.users = users

        return positionService.updateOne(oldOne, newOne)
    }
}