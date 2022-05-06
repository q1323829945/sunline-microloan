package cn.sunline.saas.rbac.controller

import cn.sunline.saas.rbac.controller.dto.DTOUserAdd
import cn.sunline.saas.rbac.controller.dto.DTOUserChange
import cn.sunline.saas.rbac.controller.dto.DTOUserView
import cn.sunline.saas.rbac.service.UserManagerService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    private lateinit var userManagerService: UserManagerService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = userManagerService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoUser: DTOUserAdd): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val user = userManagerService.addOne(dtoUser)
        return DTOResponseSuccess(user).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoUser: DTOUserChange): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        val user = userManagerService.updateOne(id,dtoUser)
        return DTOResponseSuccess(user).response()
    }
}