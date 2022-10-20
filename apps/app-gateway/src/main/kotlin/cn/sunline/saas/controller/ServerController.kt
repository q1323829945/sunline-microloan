package cn.sunline.saas.controller

import cn.sunline.saas.exception.InstanceNotFoundException
import cn.sunline.saas.exception.ServerNotFoundException
import cn.sunline.saas.modules.dto.DTOInstance
import cn.sunline.saas.modules.dto.DTOServer
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.service.GatewayService
import cn.sunline.saas.service.InstanceService
import cn.sunline.saas.service.ServerService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("server")
class ServerController {
    @Autowired
    private lateinit var serverService: ServerService

    @Autowired
    private lateinit var gatewayService: GatewayService

    @Autowired
    private lateinit var instanceService: InstanceService

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    data class DTOServerView(
        val id:String,
        val instanceId:String,
        val server:String,
        val domain:String,
        var accessKey:String? = null
    )

    @PostMapping
    fun register(@RequestBody dtoServer: DTOServer):ResponseEntity<DTOResponseSuccess<DTOServerView>>{
        val instance = instanceService.findByTenant(dtoServer.tenant)?: run {
            instanceService.register(DTOInstance(dtoServer.tenant,false))
        }
        val server = serverService.register(instance,dtoServer)
        gatewayService.addServer(instance.id,server)
        val response = objectMapper.convertValue<DTOServerView>(server)
        response.accessKey = instance.accessKey
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("{tenant}/{server}")
    fun getOne(@PathVariable tenant:String,@PathVariable server:String):ResponseEntity<DTOResponseSuccess<DTOServerView>>{
        val instance = instanceService.findByTenant(tenant)?: throw InstanceNotFoundException("Invalid instance!!")
        val instanceServer = serverService.findByInstanceAndServer(instance.id,server)?: throw ServerNotFoundException("Invalid server!!")
        val response = objectMapper.convertValue<DTOServerView>(instanceServer)
        response.accessKey = instance.accessKey
        return DTOResponseSuccess(response).response()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id:String){
        serverService.remove(id.toLong())?.run {
            gatewayService.removeServer(this.instanceId,this.id.toString())
        }

    }
}