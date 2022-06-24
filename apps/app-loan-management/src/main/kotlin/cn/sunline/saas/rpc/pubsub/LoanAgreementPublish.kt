package cn.sunline.saas.rpc.pubsub

interface LoanAgreementPublish {

    fun loanAgreementPaid(applicationId: Long)

    fun loanAgreementSigned(applicationId: Long)

    fun loanInvoiceRepaymentFinish(instructionId: Long)

    fun loanInvoiceRepaymentCancel(instructionId: Long)
}