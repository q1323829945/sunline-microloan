package cn.sunline.saas.consumer.loan.invoke.impl

import cn.sunline.saas.consumer.loan.invoke.ConsumerLoanInvoke
import cn.sunline.saas.consumer.loan.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.consumer.loan.invoke.dto.DTOLoanProduct
import org.springframework.stereotype.Component

@Component
class ConsumerLoanInvokeImpl:ConsumerLoanInvoke {
    override fun retrieveCustomerOffer(applicationId: Long): DTOCustomerOffer {
        TODO("Not yet implemented")
    }

    override fun retrieveLoanProduct(productId: Long): DTOLoanProduct {
        TODO("Not yet implemented")
    }
}