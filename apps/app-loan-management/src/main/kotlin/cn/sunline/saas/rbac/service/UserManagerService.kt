package cn.sunline.saas.rbac.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.controller.dto.DTOUserAdd
import cn.sunline.saas.rbac.controller.dto.DTOUserChange
import cn.sunline.saas.rbac.controller.dto.DTOUserView
import cn.sunline.saas.rbac.exception.UserBusinessException
import cn.sunline.saas.rbac.exception.UserNotFoundException
import cn.sunline.saas.rpc.invoke.RbacInvoke
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.services.RoleService
import cn.sunline.saas.rbac.services.UserService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class UserManagerService  {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var rbacInvoke: RbacInvoke

    fun getPaged(pageable: Pageable): Page<DTOUserView> {
        val page = userService.getPageWithTenant(pageable = pageable).map {
            DTOUserView(
                id = it.id.toString(),
                username = it.username,
                email = it.email,
                roles = objectMapper.convertValue(it.roles),
                person = rbacInvoke.getPerson(it.personId)
            )
        }
        return page
    }

    fun addOne(dtoUser: DTOUserAdd): DTOUserView {
        val user = objectMapper.convertValue<User>(dtoUser)
        val oldUser = userService.getByUsername(user.username)
        if (oldUser != null) {
            throw UserBusinessException("This user has already exist", ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        val registeredUser = userService.register(user)
        return objectMapper.convertValue(registeredUser)
    }

    fun updateOne(id: Long, dtoUser: DTOUserChange): DTOUserView {
        val oldUser = userService.getOne(id) ?: throw UserNotFoundException("Invalid user", ManagementExceptionCode.DATA_NOT_FOUND)
        val newUser = objectMapper.convertValue<User>(dtoUser)

        if (dtoUser.roleList.isEmpty()) {
            newUser.roles.clear()
        } else {
            newUser.roles = roleService.getByIds(dtoUser.roleList).toMutableList()
        }

        val savedUser = userService.updateOne(oldUser, newUser)
        return objectMapper.convertValue(savedUser)
    }
}