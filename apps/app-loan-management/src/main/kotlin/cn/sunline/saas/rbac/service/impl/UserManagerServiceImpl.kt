package cn.sunline.saas.rbac.service.impl

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.dto.DTOUserAdd
import cn.sunline.saas.rbac.dto.DTOUserChange
import cn.sunline.saas.rbac.dto.DTOUserView
import cn.sunline.saas.rbac.exception.UserNotFoundException
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.service.UserManagerService
import cn.sunline.saas.rbac.services.RoleService
import cn.sunline.saas.rbac.services.UserService
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
import org.springframework.web.bind.annotation.*
import javax.persistence.criteria.Predicate


@Service
class UserManagerServiceImpl: UserManagerService {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleService: RoleService

    override fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = userService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOUserView>(it) }).response()
    }

    override fun addOne(dtoUser: DTOUserAdd): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val user = objectMapper.convertValue<User>(dtoUser)
        val oldUser = userService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("username"), user.username))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()
        if(oldUser != null){
            throw ManagementException(ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        val registeredUser = userService.register(user)
        val responseUser = objectMapper.convertValue<DTOUserView>(registeredUser)
        return DTOResponseSuccess(responseUser).response()
    }

    override fun updateOne(id: Long, dtoUser: DTOUserChange): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val oldUser = userService.getOne(id) ?: throw UserNotFoundException("Invalid user", ManagementExceptionCode.DATA_NOT_FOUND)
        val newUser = objectMapper.convertValue<User>(dtoUser)

        if (dtoUser.roleList.isEmpty()) {
            newUser.roles.clear()
        } else {
            newUser.roles = roleService.getByIds(dtoUser.roleList).toMutableList()
        }

        val savedUser = userService.updateOne(oldUser, newUser)
        val responseUser = objectMapper.convertValue<DTOUserView>(savedUser)
        return DTOResponseSuccess(responseUser).response()
    }
}