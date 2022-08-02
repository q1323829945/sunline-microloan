package cn.sunline.saas.rpc.pubsub

interface LoanAgreementPublish {

    fun loanAgreementPaid(applicationId: Long)

    fun loanAgreementSigned(applicationId: Long)

    fun loanInvoiceRepaymentFulfill(instructionId: Long)

    fun loanInvoiceRepaymentFail(instructionId: Long)
}