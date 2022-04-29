package cn.sunline.saas.loan.service

import cn.sunline.saas.config.AppHttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.loan.dto.DTORepaymentPlanView
import org.springframework.stereotype.Service

@Service
class LoanService(val appHttpConfiguration: AppHttpConfiguration, val ipConfig: IpConfig) {

    fun getLoanCalculate(productId: Long, amount: String, term: String): DTORepaymentPlanView {
//        val uri = "http://${ipConfig.loanIp}/ConsumerLoan/$productId/$amount/$term/calculate"
//
//        val postMethod = appHttpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri,appHttpConfiguration.getPublicHeaders())
//
//        appHttpConfiguration.sendClient(postMethod)
//
//        val data = appHttpConfiguration.getResponse(postMethod)
//
//        return Gson().fromJson(data, DTORepaymentPlanView::class.java)

        return DTORepaymentPlanView("1","1", listOf())
    }

}