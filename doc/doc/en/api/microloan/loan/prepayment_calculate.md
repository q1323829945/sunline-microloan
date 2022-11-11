# Trial calculation of prepayment

## Request

| Path        | /microloan/ConsumerLoan/prepayment/{agreementId}/calculate |
| ----------- | ---------------------------------------------------------- |
| Method      | GET                                                        |
| Description | Trial calculation of prepayment                            |

### Path parameter

| Parameter   | Type   | M/O  | Description  |
| ----------- | ------ | ---- | ------------ |
| agreementId | string | M    | Agreement id |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter           | Type   | Description                                                  |
| ------------------- | ------ | ------------------------------------------------------------ |
| data                | object |                                                              |
| ∟agreementId        | string | Agreement id                                                 |
| ∟totalAmount        | string | Total amount                                                 |
| ∟prepaymentLines    | array  | Prepayments                                                  |
| ∟∟invoiceAmountType | string | Invoice amount typpe<br/>[See Appendix Dictionary Code - Invoice Amount Type](../../appendices/dictionary_code.md) |
| ∟∟invoiceAmount     | string | Invoice amount                                               |

### Example of response body

```json
{
    "data":{
        "agreementId":"123",
        "totalAmount":"5000",
        "prepaymentLines":[
            {
                "invoiceAmountType":"PRINCIPAL",
                "invoiceAmount":"4000"
            },
            {
                "invoiceAmountType":"FEE",
                "invoiceAmount":"4000"
            }
        ]
    },
    "code":0
}
```

