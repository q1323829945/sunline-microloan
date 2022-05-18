package cn.sunline.saas.consumer_loan.service.assembly

import cn.sunline.saas.rpc.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.rpc.invoke.dto.DTOLoanProduct
import cn.sunline.saas.disbursement.arrangement.model.DisbursementLendTypeimport cn.sunline.saas.disbursement.arrangement.model.dto.DTODisbursementArrangementAdd
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestRate
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementView
import cn.sunline.saas.repayment.arrangement.model.dto.DTOPrepaymentArrangementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentAccount
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangement
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangementAdd
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangementInvolvement
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * @title: ConsumerLoanAssembly
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/6 16:59
 */
object ConsumerLoanAssembly {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun convertToDTOLoanAgreementAdd(
        customerOffer: DTOCustomerOffer,
        loanProduct: DTOLoanProduct
    ): DTOLoanAgreementAdd {
        val dtoInterestArrangementAdd = loanProduct.interestFeature!!.run {
            DTOInterestArrangementAdd(
                interestType = interestType,
                baseYearDays = interestModality.baseYearDays,
                adjustFrequency = interestModality.adjustFrequency,
                overdueInterestRatePercentage = overdueInterest.overdueInterestRatePercentage,
                planRates = objectMapper.convertValue<MutableList<DTOInterestRate>>(ratePlanId)
            )
        }

        val dtoPrepayments = mutableListOf<DTOPrepaymentArrangementAdd>()
        loanProduct.repaymentFeature!!.prepayment.forEach {
            dtoPrepayments.add(
                DTOPrepaymentArrangementAdd(
                    term = it.term,
                    type = it.type,
                    penaltyRatio = it.penaltyRatio
                )
            )
        }

        val dtoRepaymentAccounts = mutableListOf<DTORepaymentAccount>()
        customerOffer.referenceAccount?.apply {
            dtoRepaymentAccounts.add(
                DTORepaymentAccount(
                    repaymentAccount = this.account,
                    repaymentAccountBank = this.accountBank
                )
            )
        }

        val dtoRepaymentArrangementAdd = loanProduct.repaymentFeature.run {
            DTORepaymentArrangementAdd(
                paymentMethod = payment.paymentMethod,
                frequency = payment.frequency,
                repaymentDayType = payment.repaymentDayType,
                prepaymentArrangement = dtoPrepayments,
                repaymentAccount = dtoRepaymentAccounts
            )
        }

        val dtoDisbursementArrangementAdd = customerOffer.referenceAccount?.run {
            DTODisbursementArrangementAdd(
                disbursementAccount = this.account,
                disbursementAccountBank = this.accountBank,
                disbursementLendType = DisbursementLendType.ONCE
            )
        }

        return DTOLoanAgreementAdd(
            productId = customerOffer.productId,
            term = customerOffer.term,
            amount = customerOffer.amount,
            currency = customerOffer.currency,
            interestArrangement = dtoInterestArrangementAdd,
            repaymentArrangement = dtoRepaymentArrangementAdd,
            feeArrangement = loanProduct.feeFeatures?.run {
                objectMapper.convertValue<MutableList<DTOFeeArrangementAdd>>(
                    this
                )
            },
            borrower = customerOffer.customerId,
            lender = mutableListOf(loanProduct.businessUnit), disbursementArrangement = dtoDisbursementArrangementAdd,
            purpose = customerOffer.purpose ?: loanProduct.loanPurpose,
            applicationId = customerOffer.applicationId,
            userId = customerOffer.userId
        )
    }

    fun convertToDTOUnderwritingArrangementAdd(
        loanAgreementAggregate: DTOLoanAgreementView,
        guarantors: MutableList<DTOGuarantor>
    ): DTOUnderwritingArrangementAdd {

        val involvements = mutableListOf<DTOUnderwritingArrangementInvolvement>()
        guarantors.forEach {
            involvements.add(DTOUnderwritingArrangementInvolvement(it.partyId, it.primaryGuarantor))
        }

        val underwritingArrangements = mutableListOf<DTOUnderwritingArrangement>()
        underwritingArrangements.add(
            DTOUnderwritingArrangement(
                loanAgreementAggregate.loanAgreement.fromDateTime.toString(),
                loanAgreementAggregate.loanAgreement.toDateTime.toString(),
                involvements
            )
        )

        return DTOUnderwritingArrangementAdd(loanAgreementAggregate.loanAgreement.id, underwritingArrangements)
    }
}