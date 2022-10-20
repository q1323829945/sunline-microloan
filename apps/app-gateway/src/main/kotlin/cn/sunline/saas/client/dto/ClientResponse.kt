package cn.sunline.saas.client.dto

import java.io.InputStream

data class ClientResponse(
    val body: InputStream,
    val headers:Map<String,String>,
    val status:Int
)