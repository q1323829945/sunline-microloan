package cn.sunline.saas.exceptions

enum class ManagementExceptionCode(val code: Int) {
    // Base 1000 - 1999
    REQUEST_INVALID_PARAMETER(1000),
    REQUEST_INCOMPLETE_PARAMETER(1001),
    REQUEST_MISSING_REQUEST_PARAMETER(1002),
    REQUEST_RESOURCE_ACCESS_DENIED(1004),
    DATA_ALREADY_EXIST(1005),
    TYPE_ERROR(1006),
    DATA_NOT_FOUND(1007),
    CONTEXT_IS_NULL(1008),
    USER_UNINITIALIZED(1009),
    TENANT_UNINITIALIZED(1010),

    // Dapr client 1200 - 1299
    DAPR_INVOCATION_NETWORK_ERROR(1200),
    DAPR_INVOCATION_REQUEST_ERROR(1201),
    DAPR_INVOCATION_SERVER_ERROR(1202),
    DAPR_INVOCATION_REDIRECT_ERROR(1203),
    DAPR_PUBSUB_NETWORK_ERROR(1205),
    DAPR_PUBSUB_FORBIDDEN_ERROR(1206),
    DAPR_PUBSUB_NO_ROUTE_ERROR(1207),
    DAPR_PUBSUB_DELIVERY_ERROR(1208),
    DAPR_ACTOR_NETWORK_ERROR(1209),
    DAPR_ACTOR_REQUEST_FAILED(1210),
    DAPR_ACTOR_NOT_FOUND_OR_MALFORMED_REQUEST(1211),
    DAPR_ACTOR_SERVER_ERROR(1212),
    DAPR_BINDINGS_NETWORK_ERROR(1213),
    DAPR_BINDINGS_FORBIDDEN_ERROR(1214),
    DAPR_BINDINGS_NO_ROUTE_ERROR(1215),
    DAPR_BINDINGS_DELIVERY_ERROR(1216),

    // scheduler 1300 - 1399
    SCHEDULER_TIMER_NOT_CONFIGURE(1300),

    // Auth 2000 - 2999
    AUTHORIZATION_LOGIN_FAILED(2000),
    AUTHORIZATION_TOKEN_VALIDATION_FAILED(2001),
    AUTHORIZATION_USERNAME_MISSING(2002),
    AUTHORIZATION_USERNAME_INVALID(2003),
    AUTHORIZATION_TENANT_DOMAIN_MISSING(2004),
    USER_NOT_FOUND(2005),
    ROLE_NOT_FOUND(2006),
    PERSON_ALREADY_BINDING(2007),
    CUSTOMER_NOT_FOUND(2008),
    POSITION_NOT_FOUND(2009),
    POSITION_CHANGE_EXCEPTION(2010),

    // Tenant 3000 - 3999
    TENANT_DATA_SOURCE_INVALID(3000),
    TENANT_NOT_FOUND(3001),


    //upload 4000 - 4999
    // Obs and Document 4000 - 4999
    FILE_UPLOAD_FAILED(4000),
    BODY_TYPE_ERROR(4001),
    DOCUMENT_TEMPLATE_NOT_FOUND(4002),
    DOCUMENT_DIRECTORY_NOT_FOUND(4003),

    // Product 5000 - 5049
    PRODUCT_NOT_FOUND(5000),
    PRODUCT_TERM_CONFIG_MAX_MIN_ERROR(5001),
    PRODUCT_AMOUNT_CONFIG_MAX_MIN_ERROR(5002),
    PRODUCT_STATUS_ERROR(5003),
    REPAYMENT_FEATURE_NOT_FOUND(5004),
    PRODUCT_TYPE_ALREADY_EXIST(5005),

    // Fee 5050 - 5099
    FEE_CONFIG_ERROR(5050),

    // Interest 5100 - 5149
    RATE_PLAN_NOT_FOUND(5100),
    INTEREST_RATE_NOT_FOUND(5101),
    RATE_PLAN_HAS_BEEN_USED(5102),

    // Http 5150 - 5199
    HTTP_ERROR(5151),

    //loan 5200 - 5299
    LOAN_UPLOAD_CONFIGURE_NOT_FOUND(5101),
    APPLICATION_ID_ALREADY_EXIST(5102),
    LOAN_APPLY_NOT_FOUND(5103),
    PRODUCT_TYPE_CANNOT_BE_CHANGE(5104),
    SUB_TYPE_IS_IRRELEVANT_PARENT_TYPE(5105),
    LOAN_APPLY_STATUS_CAN_NOT_BE_CHANGE(5106),
    SUBMIT_ERROR(5107),
    LOAN_HAS_BEEN_PRODUCT_TYPE(5108),
    //huawei cloud 5300 - 5349
    DOMAIN_BINDING_ERROR(5300),
    CERTIFICATE_BINDING_ERROR(5301),

    //underwriting 5350 - 5399
    UNDERWRITING_NOT_FOUND(5350),
    UNDERWRITING_STATUS_CANNOT_BE_UPDATE(5351),
    UNDERWRITING_CANNOT_BE_UPDATE(5352),

    //risk control rule 5400-5449
    RISK_CONTROL_RULE_NOT_FOUND(5400),

    //party 5450-5549
    ORGANISATION_NOT_FOUND(5450),
    PERSON_NOT_FOUND(5450),

    //customer offer 5550-5559
    CUSTOMER_OFFER_NOT_FOUND(5550),
    CUSTOMER_OFFER_STATUS_ERROR(5551),
    CUSTOMER_LOAN_APPLY_NOT_FOUND(5552),

    //loan agreement 5560-5599
    LOAN_AGREEMENT_NOT_FOUND(5560),
    LOAN_AGREEMENT_STATUS_CHECK(5561),
    DISBURSEMENT_ARRANGEMENT_NOT_FOUND(5562),
    DISBURSEMENT_LEND_TYPE_NOT_SUPPORTED(5563),
    DISBURSEMENT_INSTRUCTION_NOT_FOUND(5564),

    //interest arrangement 5600-5609
    BASE_RATE_NULL(5600),
    INTEREST_ARRANGEMENT_NOT_FOUND(5601),

    //repayment arrangement 5610-5619
    REPAYMENT_ARRANGEMENT_NOT_FOUND(5610),
    REPAYMENT_INSTRUCTION_NOT_FOUND(5611),
    REPAYMENT_INSTRUCTION_ERROR(5612),
    REPAYMENT_INSTRUCTION_STATUS_ERROR(5613),

    //invoice arrangement 5620-5629
    INVOICE_ARRANGEMENT_NOT_FOUND(5620),

    //invoice 5630-5639
    INVOICE_NOT_FOUND(5630),
    INVOICE_STATUS_ERROR(5631),

    //invoice 5640-5649
    ACCOUNT_NOT_FOUND(5640),
    ACCOUNT_BALANCE_NEGATE(5641),

    //pdpa 5650-5659
    PDPA_ALREADY_EXIST(5650),
    PDPA_NOT_FOUND(5651),
    CUSTOMER_PDPA_INFORMATION_NOT_FOUND(5652),
    PDPA_AUTHORITY_NOT_FOUND(5653),

    //questionnaire 5660-5669
    QUESTIONNAIRE_NOT_FOUND(5060),


    //channel agreement 5670-5699
    CHANNEL_NOT_FOUND(5670),
    CHANNEL_ARRANGEMENT_NOT_FOUND(5671),
    CHANNEL_AGREEMENT_NOT_FOUND(5672)
}