package cn.sunline.saas.controller

import cn.sunline.saas.exception.ApiNotFoundException
import cn.sunline.saas.modules.dto.DTOApi
import cn.sunline.saas.modules.enum.FormatType
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.service.ApiService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class ApiController {
    @Autowired
    private lateinit var apiService: ApiService
    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    data class DTOApiView(
        val id:String,
        val serverId: String,
        val api:String,
        val method:String,
        val formatType: FormatType,
        val enable:Boolean,
    )

    @PostMapping
    fun addOne(@RequestBody dtoApi: DTOApi):ResponseEntity<DTOResponseSuccess<DTOApiView>>{
        val api = apiService.addOne(dtoApi)
        val response = objectMapper.convertValue<DTOApiView>(api)
        return DTOResponseSuccess(response).response()
    }

    @PutMapping
    fun updateOne(@RequestBody dtoApi: DTOApi):ResponseEntity<DTOResponseSuccess<DTOApiView>>{
        val api = apiService.updateOne(dtoApi)?: throw ApiNotFoundException("Invalid api!")
        val response = objectMapper.convertValue<DTOApiView>(api)
        return DTOResponseSuccess(response).response()
    }

    @DeleteMapping
    fun deleteOne(@RequestBody dtoApi: DTOApi):ResponseEntity<DTOResponseSuccess<Boolean>>{
        apiService.deleteOne(dtoApi)
        return DTOResponseSuccess(true).response()
    }
}