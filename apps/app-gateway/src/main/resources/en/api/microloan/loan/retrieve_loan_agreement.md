# Borrowing details

## Request

| Path        | /microloan/ConsumerLoan/LoanAgreement/detail/{agreementId}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | Borrowing details                                            |

### Path parameter

| Parameter   | Type    | M/O  | Description  |
| ----------- | ------- | ---- | ------------ |
| agreementId | integer | M    | Agreement id |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter            | Type   | Description                                                  |
| -------------------- | ------ | ------------------------------------------------------------ |
| data                 | object |                                                              |
| ∟agreementId         | string | Agreement id                                                 |
| ∟productName         | string | Product name                                                 |
| ∟amount              | string | amount                                                       |
| ∟term                | string | Term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟disbursementAccount | string | Disbursement account                                         |
| ∟purpose             | string | Purpose                                                      |
| ∟paymentMethod       | string | Payment method <br/>[See Appendix Dictionary Code -Payment Method Type](../../appendices/dictionary_code.md) |
| ∟disbursementBank    | string | Disbursement bank                                            |
| ∟agreementDocumentId | string | Agreement document id                                        |

### Example of response body

```json
{
    "data":{
        "agreementId":"123",
        "productName":"product",
        "amount":"500",
        "term":"ONE_MONTH",
        "disbursementAccount":"9000",
        "purpose":"purpose",
        "paymentMethod":"EQUAL_INSTALLMENT",
        "disbursementBank":"ABC Bank",
        "agreementDocumentId":"123"
    },
    "code":0
}
```

