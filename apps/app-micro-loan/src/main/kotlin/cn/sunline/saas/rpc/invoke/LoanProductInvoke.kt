package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.rpc.invoke.dto.DTOLoanProduct


/**
 * @title: LoanProductInvoke
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 15:52
 */
interface LoanProductInvoke {

    fun getOneById(id: Long): DTOLoanProduct

    fun getOneByIdentificationCode(identificationCode: String): DTOLoanProduct
}