package cn.sunline.saas.consumer_loan.event

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstruction

/**
 * @title: ConsumerLoanPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:08
 */
interface ConsumerLoanPublish {

    fun initiatePositionKeeping(dtoBankingTransaction: DTODisbursementInstruction)

    fun financialAccounting(dtoBankingTransaction: DTODisbursementInstruction)

    fun disbursement(dtoBankingTransaction: DTODisbursementInstruction)
}