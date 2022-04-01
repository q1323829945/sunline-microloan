package cn.sunline.saas.repayment.schedule.factory

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.repayment.schedule.factory.impl.EqualPrincipalCalculator
import cn.sunline.saas.repayment.schedule.factory.impl.EqualInstallmentCalculator
import cn.sunline.saas.repayment.schedule.factory.impl.PayInterestSchedulePrincipalMaturityCalculator
import cn.sunline.saas.repayment.schedule.factory.impl.OneOffRepaymentCalculator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CalculatorFactory {

    @Autowired
    private lateinit var equalPrincipalCalculator: EqualPrincipalCalculator

    @Autowired
    private lateinit var equalInstallmentCalculator: EqualInstallmentCalculator

    @Autowired
    private lateinit var payInterestSchedulePrincipalMaturityCalculator: PayInterestSchedulePrincipalMaturityCalculator

    @Autowired
    private lateinit var oneOffRepaymentCalculator: OneOffRepaymentCalculator

    fun instance(repaymentType: PaymentMethodType): BaseRepaymentScheduleCalculator {
        return when (repaymentType) {
            PaymentMethodType.EQUAL_PRINCIPAL -> equalPrincipalCalculator
            PaymentMethodType.EQUAL_INSTALLMENT -> equalInstallmentCalculator
            PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> payInterestSchedulePrincipalMaturityCalculator
            PaymentMethodType.ONE_OFF_REPAYMENT -> oneOffRepaymentCalculator
        }
    }
}