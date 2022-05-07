package cn.sunline.saas.rpc.impl

import cn.sunline.saas.rpc.LoanProductInvoke
import cn.sunline.saas.rpc.dto.DTOLoanProduct
import org.springframework.stereotype.Component

/**
 * @title: LoanProductInvokeImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 16:28
 */
@Component
class LoanProductInvokeImpl : LoanProductInvoke {
    override fun getOneById(id: Long): DTOLoanProduct {
        TODO("Not yet implemented")
    }

    override fun getOneByIdentificationCode(identificationCode: String): DTOLoanProduct {
        TODO("Not yet implemented")
    }
}