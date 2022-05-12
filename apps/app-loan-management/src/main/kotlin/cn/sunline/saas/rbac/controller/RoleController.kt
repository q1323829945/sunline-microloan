package cn.sunline.saas.rbac.controller

import cn.sunline.saas.rbac.controller.dto.DTORoleChange
import cn.sunline.saas.rbac.controller.dto.DTORoleView
import cn.sunline.saas.rbac.service.RoleManagerService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("roles")
class RoleController {

    @Autowired
    private lateinit var roleManagerService: RoleManagerService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = roleManagerService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("all")
    fun getAll(): ResponseEntity<DTOPagedResponseSuccess> {
        val page = roleManagerService.getAll()
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        val role = roleManagerService.addOne(dtoRole)
        return DTOResponseSuccess(role).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: String, @RequestBody dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        val role = roleManagerService.updateOne(id.toLong(),dtoRole)
        return DTOResponseSuccess(role).response()
    }
}