

package cn.sunline.saas.repayment.schedule.factory

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.repayment.schedule.factory.impl.EqualInstallmentImpl
import cn.sunline.saas.repayment.schedule.factory.impl.EqualPrincipalImpl
import cn.sunline.saas.repayment.schedule.factory.impl.OneOffRepaymentImpl
import cn.sunline.saas.repayment.schedule.factory.impl.PayInterestSchedulePrincipalMaturityImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CalculatorFactory {

    @Autowired
    private lateinit var equalPrincipalImpl: EqualPrincipalImpl

    @Autowired
    private lateinit var equalInstallmentImpl: EqualInstallmentImpl

    @Autowired
    private lateinit var payInterestSchedulePrincipalMaturityImpl: PayInterestSchedulePrincipalMaturityImpl

    @Autowired
    private lateinit var oneOffRepaymentImpl: OneOffRepaymentImpl

    fun instance(repaymentType: PaymentMethodType): BaseRepaymentScheduleService {
        return when (repaymentType) {
            PaymentMethodType.EQUAL_PRINCIPAL -> equalPrincipalImpl
            PaymentMethodType.EQUAL_INSTALLMENT -> equalInstallmentImpl
            PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> payInterestSchedulePrincipalMaturityImpl
            PaymentMethodType.ONE_OFF_REPAYMENT -> oneOffRepaymentImpl
        }
    }
}