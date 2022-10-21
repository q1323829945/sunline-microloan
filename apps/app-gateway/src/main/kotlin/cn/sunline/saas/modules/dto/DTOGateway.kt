package cn.sunline.saas.modules.dto

import cn.sunline.saas.modules.enum.FormatType
import org.apache.catalina.core.ApplicationPart
import java.io.InputStream

data class SendContext(
    val url:String,
    val path:String,
    val server:String,
    val query:Map<String,String>? = null,
    val headers:Map<String,String>? = null,
    val parts:List<ApplicationPart>? = null,
    var method:String,
    val formatType: FormatType,
    val body:InputStream? = null,
    val tenant:String,
    val queryString:String? = null
)

data class DTOGateway(
    val url:String,
    val path:String,
    val server:String,
    val secretKey:String,
    val tenant:String,
)

data class TenantInstance(
    val instanceId:String,
    val secretKey: String,
    val tenant:String,
    val server:MutableList<TenantServer>
)

data class TenantServer(
    val domain:String,
    val server:String,
    val serverId:String,
    val apis:MutableList<TenantApi>
)

data class TenantApi(
    val path:String,
    val pathGroup:List<String>,
    val method:String,
    val formatType: FormatType
)