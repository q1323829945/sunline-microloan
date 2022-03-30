package cn.sunline.saas.rbac.controller

import cn.sunline.saas.config.PermissionConfig
import cn.sunline.saas.menu.services.MenuService
import cn.sunline.saas.rbac.exception.UserNotFoundException
import cn.sunline.saas.rbac.services.UserService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("menus")
class MenuController {
    data class DTOMenuView(
            val name:String,
            val parentName:String,
            val icon:String,
            val path:String,
            val children:List<DTOMenuView>?
    )

    @Autowired
    private lateinit var menuService: MenuService

    @Autowired
    private lateinit var userService: UserService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getMenu(@RequestHeader("X-Authorization-Username")username:String): ResponseEntity<DTOResponseSuccess<List<DTOMenuView>>> {
        val user = userService.getByUsername(username)?:throw UserNotFoundException("Invalid user")

        val permissionSetList = user.roles.map {
            it.permissions
        }.toSet()

        val menuNameSet:HashSet<String> = hashSetOf()
        for(permissionList in permissionSetList){
            for(permission in permissionList){
                menuNameSet.add(PermissionConfig.valueOf(permission.name).resource)
            }
        }

        if(menuNameSet.size == 0){
            return DTOResponseSuccess(listOf<DTOMenuView>()).response()
        }

        val resultMenu = menuService.getPermissionMenu(menuNameSet)

        val responseMenu = objectMapper.convertValue<List<DTOMenuView>>(resultMenu)
        return DTOResponseSuccess(responseMenu).response()
    }
}
