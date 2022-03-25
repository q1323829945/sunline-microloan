package cn.sunline.saas.services

import cn.sunline.saas.config.HttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.customer.offer.modules.dto.DTOProductView
import cn.sunline.saas.global.constant.HttpRequestMethod
import com.google.gson.Gson
import org.springframework.stereotype.Service

@Service
class ProductService(
    private var ipConfig: IpConfig,
    private var httpConfiguration: HttpConfiguration
)  {

    fun findById(productId: Long): DTOProductView {
        val uri = "http://${ipConfig.productIp}/LoanProduct/$productId/retrieve"

        val postMethod = httpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri)

        httpConfiguration.sendClient(postMethod)

        val data = httpConfiguration.getResponse(postMethod)

        return Gson().fromJson(data, DTOProductView::class.java)
    }

}
