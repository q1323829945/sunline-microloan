package cn.sunline.saas.loan.service

import cn.sunline.saas.config.AppHttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.dto.DTORepaymentPlanView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailTrialView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class LoanService(val appHttpConfiguration: AppHttpConfiguration, val ipConfig: IpConfig) {

    fun getLoanCalculate(productId: Long, amount: BigDecimal, term: LoanTermType): DTORepaymentScheduleTrialView {
//        val uri = "http://${ipConfig.loanIp}/ConsumerLoan/$productId/$amount/$term/calculate"
//
//        val postMethod = appHttpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri,appHttpConfiguration.getPublicHeaders())
//
//        appHttpConfiguration.sendClient(postMethod)
//
//        val data = appHttpConfiguration.getResponse(postMethod)
//
//        return Gson().fromJson(data, DTORepaymentPlanView::class.java)

        return DTORepaymentScheduleTrialView(amount,amount, ArrayList<DTORepaymentScheduleDetailTrialView>())
    }

}