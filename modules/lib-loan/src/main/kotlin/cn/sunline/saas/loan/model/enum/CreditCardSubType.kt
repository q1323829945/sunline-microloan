package cn.sunline.saas.loan.model.enum

enum class CreditCardSubType(val creditCardTypes: List<CreditCardType>) {
    SHOPMORE(mutableListOf(CreditCardType.MASTERCARD)),
    STANDARD(mutableListOf(CreditCardType.MASTERCARD)),
    BENCH(mutableListOf(CreditCardType.MASTERCARD)),
    GOLD(mutableListOf(CreditCardType.MASTERCARD,CreditCardType.VISA,CreditCardType.UNIONPAY,CreditCardType.JCB)),
    TIANIUM(mutableListOf(CreditCardType.MASTERCARD)),
    PLATINUM(mutableListOf(CreditCardType.MASTERCARD,CreditCardType.VISA,CreditCardType.JCB,CreditCardType.AMERICAN_EXPRESS)),
    INSTALLMENT_CARD(mutableListOf(CreditCardType.MASTERCARD)),
    CLASSIC(mutableListOf(CreditCardType.VISA)),
    DIAMOND(mutableListOf(CreditCardType.UNIONPAY)),
    LUCKY_CAT(mutableListOf(CreditCardType.JCB)),
    INTERNATIONAL(mutableListOf(CreditCardType.DINERS_CLUB)),
    PREMIERE(mutableListOf(CreditCardType.DINERS_CLUB)),
    BULE(mutableListOf(CreditCardType.AMERICAN_EXPRESS)),
    CASHBACK(mutableListOf(CreditCardType.AMERICAN_EXPRESS)),
}