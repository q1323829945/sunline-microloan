package cn.sunline.saas.consumer_loan.service.assembly

import cn.sunline.saas.consumer_loan.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.consumer_loan.invoke.dto.DTOGuarantor
import cn.sunline.saas.consumer_loan.invoke.dto.DTOLoanProduct
import cn.sunline.saas.disbursement.arrangement.model.DisbursementLendType
import cn.sunline.saas.disbursement.arrangement.model.dto.DTODisbursementArrangementAdd
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestRate
import cn.sunline.saas.invoice.arrangement.service.DTOInvoiceArrangement
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementView
import cn.sunline.saas.repayment.arrangement.model.dto.DTOPrepaymentArrangementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentAccount
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd
import cn.sunline.saas.schedule.Schedule
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
        loanProduct: DTOLoanProduct,
        baseRate: String?
    ): DTOLoanAgreementAdd {
        val dtoInterestArrangementAdd = loanProduct.interestFeature.run {
            DTOInterestArrangementAdd(
                interestType = interestType,
                baseYearDays = interest.baseYearDays,
                adjustFrequency = interest.adjustFrequency,
                overdueInterestRatePercentage = overdueInterest.overdueInterestRatePercentage,
                planRates = objectMapper.convertValue<MutableList<DTOInterestRate>>(ratePlan),
                baseRate = baseRate
            )
        }

        val dtoPrepayments = mutableListOf<DTOPrepaymentArrangementAdd>()
        loanProduct.repaymentFeature.prepayment.forEach {
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

        val dtoInvoiceArrangement = DTOInvoiceArrangement(
            invoiceDay = null,
            repaymentDay = null,
            graceDays = loanProduct.repaymentFeature.payment.graceDays
        )

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
            lender = mutableListOf(loanProduct.businessUnit),
            disbursementArrangement = dtoDisbursementArrangementAdd,
            purpose = customerOffer.purpose ?: loanProduct.loanPurpose,
            applicationId = customerOffer.applicationId,
            userId = customerOffer.userId,
            invoiceArrangement = dtoInvoiceArrangement
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

    fun convertToDTOLoanInvoice(
        schedule: MutableList<Schedule>,
        dtoLoanAgreement: DTOLoanAgreementView
    ): MutableList<DTOLoanInvoice> {
        val dtoLoanInvoice = mutableListOf<DTOLoanInvoice>()
        schedule.forEach {
            dtoLoanInvoice.add(
                DTOLoanInvoice(
                    it.fromDate.toString(),
                    it.dueDate.toString(),
                    dtoLoanAgreement.loanAgreement.involvements.first { involvement -> LoanAgreementInvolvementType.LOAN_BORROWER == involvement.involvementType }.partyId,
                    it.principal,
                    it.interest,
                    null,
                    dtoLoanAgreement.loanAgreement.id
                )
            )
        }
        return dtoLoanInvoice
    }

    fun convertToDTORepaymentAccountAdd(
        repaymentAccount: String,
        repaymentAccountBank: String
    ): MutableList<DTORepaymentAccount> {
        val dtoRepaymentAccounts = mutableListOf<DTORepaymentAccount>()
        dtoRepaymentAccounts.add(
            DTORepaymentAccount(
                repaymentAccount = repaymentAccount,
                repaymentAccountBank = repaymentAccountBank
            )
        )
        return dtoRepaymentAccounts
    }
}