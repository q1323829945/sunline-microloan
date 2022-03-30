package cn.sunline.saas.rbac.controller

import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.rbac.exception.UserNotFoundException
import cn.sunline.saas.rbac.modules.User
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
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("users")
class UserController {
    data class DTORoleView(val id:Long,val name: String)
    data class DTOUserView(val id: Long, val username: String, val email: String, val roles: List<DTORoleView>)
    data class DTOUserAdd(
            val username: String,
            val email: String,
            val password: String,
    )
    data class DTOUserChange(
            val email: String,
            val roleList: List<Long> = listOf()
    )

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleService: RoleService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = userService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOUserView>(it) }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoUser: DTOUserAdd): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val user = objectMapper.convertValue<User>(dtoUser)
        val registeredUser = userService.register(user)
        val responseUser = objectMapper.convertValue<DTOUserView>(registeredUser)
        return DTOResponseSuccess(responseUser).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoUser: DTOUserChange): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val oldUser = userService.getOne(id) ?: throw UserNotFoundException("Invalid user")
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