package cn.sunline.saas.rbac.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.PositionType
import cn.sunline.saas.channel.rbac.exception.PositionNotFoundException
import cn.sunline.saas.rbac.exception.UserBusinessException
import cn.sunline.saas.rbac.exception.UserNotFoundException
import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.modules.dto.DTOUserAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTOUserChange
import cn.sunline.saas.channel.rbac.modules.dto.DTOUserView
import cn.sunline.saas.channel.rbac.services.PositionService
import cn.sunline.saas.channel.rbac.services.RoleService
import cn.sunline.saas.channel.rbac.services.UserService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate


@Service
class UserManagerService  {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var positionService: PositionService

    @Autowired
    private lateinit var roleService: RoleService

    fun getPaged(username:String?,position: String?,unPosition:Boolean,pageable: Pageable): Page<DTOUserView> {
        val page = userService.getPaged({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            val positionTable = root.join<User,Position>("position",JoinType.LEFT)
            position?.run {
                predicates.add(criteriaBuilder.equal(positionTable.get<String>("id"),position))
            }
            if(unPosition){
                predicates.add(criteriaBuilder.isNull(positionTable.get<String>("id")))
            }
            username?.run { predicates.add(criteriaBuilder.like(root.get("username"),"$username%")) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable = pageable).map {
            convertToDTOUserView(it)
        }
        return page
    }

    fun addOne(dtoUser: DTOUserAdd): DTOUserView {
        val user = objectMapper.convertValue<User>(dtoUser)
        val oldUser = userService.getByUsername(user.username)
        if (oldUser != null) {
            throw UserBusinessException("This user has already exist", ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        dtoUser.positionId?.run {
            val position = positionService.getOne(this)?: throw PositionNotFoundException("Invalid position")
            user.position = position
        }

        val registeredUser = userService.register(user)
        return convertToDTOUserView(registeredUser)
    }

    fun updateOne(id: Long, dtoUser: DTOUserChange): DTOUserView {
        val oldUser = userService.getOne(id) ?: throw UserNotFoundException("Invalid user", ManagementExceptionCode.DATA_NOT_FOUND)
        val newUser = objectMapper.convertValue<User>(dtoUser)

        if (dtoUser.roleList.isEmpty()) {
            newUser.roles.clear()
        } else {
            newUser.roles = roleService.getByIds(dtoUser.roleList).toMutableList()
        }

        dtoUser.positionId?.run {
            val position = positionService.getOne(this)?: throw PositionNotFoundException("Invalid position")
            newUser.position = position
        }

        val savedUser = userService.updateOne(oldUser, newUser)
        return convertToDTOUserView(savedUser)
    }

    private fun convertToDTOUserView(user: User):DTOUserView{
        return DTOUserView(
            id = user.id.toString(),
            username = user.username,
            email = user.email,
            roles = objectMapper.convertValue(user.roles),
            position = user.position?.run { objectMapper.convertValue(this) }
        )
    }
}