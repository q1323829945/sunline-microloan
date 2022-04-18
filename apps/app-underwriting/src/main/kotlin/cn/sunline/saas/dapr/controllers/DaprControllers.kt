package cn.sunline.saas.dapr.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("dapr")
class DaprControllers {

    @GetMapping("/subscribe")
    fun subscribe(){
    }
}