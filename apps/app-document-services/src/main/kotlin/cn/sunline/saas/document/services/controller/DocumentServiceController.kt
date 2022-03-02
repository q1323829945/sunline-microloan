package cn.sunline.saas.document.services.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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

    @PostMapping("/generate")
    fun generate(){

    }
}