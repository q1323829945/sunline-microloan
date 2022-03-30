package cn.sunline.saas.loan.service

import cn.sunline.saas.config.HttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.loan.dto.DTORepaymentPlanView
import com.google.gson.Gson
import org.springframework.stereotype.Service

@Service
class LoanService(val httpConfiguration: HttpConfiguration,val ipConfig: IpConfig) {

    fun getLoanCalculate(productId: Long, amount: String, term: String): DTORepaymentPlanView {
        val uri = "http://${ipConfig.loanIp}/loan/$productId/$amount/$term/calculate"

        val postMethod = httpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri)

        httpConfiguration.sendClient(postMethod)

        val data = httpConfiguration.getResponse(postMethod)

        return Gson().fromJson(data, DTORepaymentPlanView::class.java)
    }

}