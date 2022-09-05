package cn.sunline.saas.rbac.controller

import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionChange
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionView
import cn.sunline.saas.rbac.service.PositionManagerService
import cn.sunline.saas.channel.rbac.services.PositionService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("position")
class PositionController {
    @Autowired
    private lateinit var positionService: PositionService

    @Autowired
    private lateinit var positionManagerService: PositionManagerService

    @GetMapping
    fun paged(@PathParam("name")name:String?,
              pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = positionService.paged(name,pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoPositionAdd: DTOPositionAdd):ResponseEntity<DTOResponseSuccess<DTOPositionView>>{
        val position = positionService.addOne(dtoPositionAdd)
        return DTOResponseSuccess(position).response()
    }

    @PutMapping
    fun updateOne(@RequestBody dtoPositionChange: DTOPositionChange):ResponseEntity<DTOResponseSuccess<DTOPositionView>>{
        val position = positionManagerService.updateOne(dtoPositionChange)
        return DTOResponseSuccess(position).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id:String):ResponseEntity<DTOResponseSuccess<DTOPositionView>>{
        val position = positionService.getDetails(id)
        return DTOResponseSuccess(position).response()
    }


    @GetMapping("all")
    fun getAll(@PathParam("name")name:String?):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = positionService.paged(name, Pageable.unpaged())
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

}