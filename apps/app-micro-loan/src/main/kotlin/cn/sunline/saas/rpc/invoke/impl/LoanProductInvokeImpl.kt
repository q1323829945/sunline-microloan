package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.rpc.invoke.LoanProductInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProduct
import org.springframework.stereotype.Component

/**
 * @title: LoanProductInvokeImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 16:28
 */
@Component
class LoanProductInvokeImpl : LoanProductInvoke {
    override fun getOneById(id: Long): DTOInvokeLoanProduct {
        TODO("Not yet implemented")
    }

    override fun getOneByIdentificationCode(identificationCode: String): DTOInvokeLoanProduct {
        TODO("Not yet implemented")
    }
}