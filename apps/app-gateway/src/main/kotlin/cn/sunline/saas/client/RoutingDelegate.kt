package cn.sunline.saas.client

import cn.sunline.saas.client.dto.ClientResponse
import cn.sunline.saas.modules.dto.DTOStatistics
import cn.sunline.saas.modules.dto.SendContext
import cn.sunline.saas.modules.enum.FormatType
import cn.sunline.saas.service.StatisticsService
import org.springframework.http.MediaType
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

class RoutingDelegate (private val client: Client,private val context: SendContext,private val statisticsService: StatisticsService) {

    private val ignoreHeader = mutableListOf(
        "connection", "content-length", "Transfer-Encoding"
    )

    fun response(response: ServletResponse) {
        val httpServletResponse = response as HttpServletResponse
        when (context.formatType) {
            FormatType.Json -> sendJson(httpServletResponse)
            FormatType.FormatData -> sendFormatData(httpServletResponse)
        }
    }

    private fun sendJson(httpServletResponse: HttpServletResponse) {
        val clientResponse = client.send(context)
        setResponse(httpServletResponse, clientResponse)
    }

    private fun sendFormatData(httpServletResponse: HttpServletResponse) {
        val clientResponse = client.sendFormatData(context)
        setResponse(httpServletResponse, clientResponse)
    }

    private fun setResponse(
        httpServletResponse: HttpServletResponse,
        clientResponse: ClientResponse?
    ) {
        clientResponse?.run {
            headers.forEach {
                if (!ignoreHeader.contains(it.key)) {
                    httpServletResponse.addHeader(it.key, it.value)
                }
            }
            if (headers["Content-Disposition"].isNullOrEmpty()) {
                httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
            }
            httpServletResponse.status = this.status
            httpServletResponse.outputStream.write(this.body.readAllBytes())

            if(this.status.toString().startsWith("20")){
                toStatistics()
            }
        }
    }

    private fun toStatistics(){
        statisticsService.addOne(
            DTOStatistics(
                tenant = context.tenant,
                server = context.server,
                method = context.method,
                path = context.path,
                query = context.queryString
            )
        )
    }
}