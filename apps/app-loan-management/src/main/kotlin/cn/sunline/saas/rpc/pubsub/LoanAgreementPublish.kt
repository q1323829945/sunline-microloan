package cn.sunline.saas.rpc.pubsub

import cn.sunline.saas.rpc.pubsub.dto.DTOLoanAgreement

interface LoanAgreementPublish {

    fun updateLoanAgreementStatus(dtoLoanAgreement: DTOLoanAgreement)
}