package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationDetail

interface LoanAgreementPublish {

    fun loanAgreementPaid(applicationId: Long)

    fun loanAgreementSigned(applicationId: Long)

    fun loanInvoiceRepaymentFulfill(instructionId: Long)

    fun loanInvoiceRepaymentFail(instructionId: Long)

    fun addLoanApplicationDetailAndStatistics(DTOLoanApplicationDetail: DTOLoanApplicationDetail)
}