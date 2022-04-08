package cn.sunline.saas.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class IpConfig {

    @Value("\${ipconfig.pdpa}")
    lateinit var pdpaIp:String


    @Value("\${ipconfig.product}")
    lateinit var productIp:String


    @Value("\${ipconfig.loan}")
    lateinit var loanIp:String


}