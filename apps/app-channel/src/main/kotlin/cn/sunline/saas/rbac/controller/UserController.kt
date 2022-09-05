package cn.sunline.saas.rbac.controller

import cn.sunline.saas.channel.rbac.modules.dto.DTOUserAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTOUserChange
import cn.sunline.saas.channel.rbac.modules.dto.DTOUserView
import cn.sunline.saas.rbac.service.UserManagerService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    private lateinit var userManagerService: UserManagerService

    @GetMapping
    fun getPaged(@PathParam("username")username:String?,
                 @PathParam("position")position: String?,
                 @PathParam("unPosition")unPosition:Boolean = false,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = userManagerService.getPaged(username,position,unPosition, pageable = pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoUser: DTOUserAdd): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val user = userManagerService.addOne(dtoUser)
        return DTOResponseSuccess(user).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: String, @RequestBody dtoUser: DTOUserChange): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val user = userManagerService.updateOne(id.toLong(),dtoUser)
        return DTOResponseSuccess(user).response()
    }

    @GetMapping("all")
    fun getAll(@PathParam("username")username:String?,
               @PathParam("position")position: String?,
               @PathParam("unPosition")unPosition:Boolean = false): ResponseEntity<DTOPagedResponseSuccess> {
        val page = userManagerService.getPaged(username,position,unPosition,Pageable.unpaged())
        return DTOPagedResponseSuccess(page.map { it }).response()
    }
}