package cn.sunline.saas.controllers

import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.services.RoleService
import cn.sunline.saas.rbac.services.UserService
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
    data class DTORoleView(val name: String)
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
    fun getPaged(pageable: Pageable): ResponseEntity<Any> {
        return ResponseEntity.ok(
                userService.getPaged(pageable = pageable).map { objectMapper.convertValue<DTOUserView>(it) })
    }

    @PostMapping
    fun addOne(@RequestBody dtoUser: DTOUserAdd): ResponseEntity<DTOUserView> {
        val user = objectMapper.convertValue<User>(dtoUser)
        val registeredUser = userService.register(user)
        val responseUser = objectMapper.convertValue<DTOUserView>(registeredUser)
        return ResponseEntity.ok(responseUser)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoUser: DTOUserChange): ResponseEntity<DTOUserView> {
        val oldUser = userService.getOne(id) ?: throw Exception("Invalid user")
        val newUser = objectMapper.convertValue<User>(dtoUser)

        if (dtoUser.roleList.isEmpty()) {
            newUser.roles.clear()
        } else {
            newUser.roles = roleService.getByIds(dtoUser.roleList).toMutableList()
        }

        val savedUser = userService.updateOne(oldUser, newUser)
        val responseUser = objectMapper.convertValue<DTOUserView>(savedUser)
        return ResponseEntity.ok(responseUser)
    }
}