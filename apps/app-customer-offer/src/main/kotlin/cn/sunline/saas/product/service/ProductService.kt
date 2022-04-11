package cn.sunline.saas.product.service

import cn.sunline.saas.config.AppHttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.customer.offer.modules.dto.DTOProductView
import cn.sunline.saas.global.constant.HttpRequestMethod
import com.google.gson.Gson
import org.springframework.stereotype.Service

@Service
class ProductService(
    private var ipConfig: IpConfig,
    private var appHttpConfiguration: AppHttpConfiguration
)  {

    fun findById(productId: Long): DTOProductView {
        val uri = "http://${ipConfig.productIp}/LoanProduct/$productId"

        val postMethod = appHttpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri,appHttpConfiguration.getPublicHeaders())

        appHttpConfiguration.sendClient(postMethod)

        val data = appHttpConfiguration.getResponse(postMethod)

        val dtoProductView = Gson().fromJson(data, DTOProductView::class.java)
        dtoProductView.productId = productId

        return dtoProductView
    }

    fun retrieve(identificationCode:String):DTOProductView{
        val uri = "http://${ipConfig.productIp}/LoanProduct/$identificationCode/retrieve"

        val postMethod = appHttpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri,appHttpConfiguration.getPublicHeaders())

        appHttpConfiguration.sendClient(postMethod)

        val data = appHttpConfiguration.getResponse(postMethod)

        return Gson().fromJson(data, DTOProductView::class.java)
    }

}
