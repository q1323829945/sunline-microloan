package cn.sunline.saas.document.generate.controller

import cn.sunline.saas.document.generate.service.DocumentGenerationService
import cn.sunline.saas.document.generate.service.dto.DTOGeneration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
/**
 * @title: DocumentServiceController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 17:30
 */
@RestController
@RequestMapping("/document-services")
class DocumentServiceController {

    @Autowired
    private lateinit var documentGenerationService:DocumentGenerationService

    @PostMapping("/generate")
    fun generate(@RequestBody dtoGeneration: DTOGeneration){
        documentGenerationService.generate(dtoGeneration)

    }

}