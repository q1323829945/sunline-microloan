package cn.sunline.saas.consumer_loan.event

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstruction
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstruction

/**
 * @title: ConsumerLoanPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:08
 */
interface ConsumerLoanPublish {

    fun initiatePositionKeeping(dtoBankingTransaction: DTOBankingTransaction)

    fun reducePositionKeeping(dtoBankingTransaction: DTOBankingTransaction)

    fun financialAccounting(dtoBankingTransaction: DTODisbursementInstruction)

    fun disbursement(dtoBankingTransaction: DTODisbursementInstruction)

    fun financialAccountingRepayment(dtoRepaymentInstruction: DTORepaymentInstruction)

    fun repayment(dtoRepaymentInstruction: DTORepaymentInstruction)

    fun financialAccountingPrepayment(dtoRepaymentInstruction: DTORepaymentInstruction)

    fun prepayment(dtoRepaymentInstruction: DTORepaymentInstruction)

    fun feePayment(dtoRepaymentInstruction: DTORepaymentInstruction)
}