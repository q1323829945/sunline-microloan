package cn.sunline.saas.underwriting.event

/**
 * @title: UnderwritingPublishTopic
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 15:46
 */
enum class UnderwritingPublishTopic {
   RETRIEVE_CUSTOMER_CREDIT_RATING,
   EXECUTE_CREDIT_RISK,
   EXECUTE_REGULATORY_COMPLIANCE,
   FRAUD_EVALUATION,
   CUSTOMER_OFFER_STATUS,
   INITIATE_LOAN_AGREEMENT
}