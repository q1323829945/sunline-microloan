# Query repayment record

## Request

| Path        | /microloan/ConsumerLoan/repayment/record/{customerId}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | Query repayment record                                       |

### Path parameter

| Parameter  | Type    | M/O  | Description |
| ---------- | ------- | ---- | ----------- |
| customerId | integer | M    | Customer id |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter        | Type    | Description                                                  |
| ---------------- | ------- | ------------------------------------------------------------ |
| data             | array   |                                                              |
| ∟id              | string  |                                                              |
| ∟repaymentAmount | decimal | Repayment amount                                             |
| ∟currencyType    | string  | Currency<br/>[See Appendix Currency Code](../../appendices/currency_code.md) |
| ∟status          | string  | Instruction lifecycle status<br/>[See Appendix Dictionary Code - Instruction Lifecycle Status](../../appendices/dictionary_code.md) |
| ∟payerAccount    | string  | Payer account                                                |
| ∟agreementId     | string  | Agreement id                                                 |
| ∟userId          | string  | User id                                                      |
| ∟customerId      | string  | Customer id                                                  |
| ∟referenceId     | string  | Reference id                                                 |
| ∟startDateTime   | string  | Start date time                                              |
| ∟endDateTime     | string  | End date time                                                |
| ∟executeDateTime | string  | Execute date time                                            |

### Example of response body

```json
{
    "data":[
        {
            "id":"123",
            "repaymentAmount":50000,
            "status":"FULFILLED",
            "currencyType":"CHN",
            "payerAccount":"123",
            "agreementId":"123",
            "userId":"123",
            "customerId":"123",
            "referenceId":"123",
            "startDateTime":"2010-06-07T15:20:00.325Z",
            "endDateTime":"2010-06-07T15:20:00.325Z",
            "executeDateTime":"2010-06-07T15:20:00.325Z"
        },
        {
            "id":"123",
            "repaymentAmount":50000,
            "status":"FULFILLED",
            "currencyType":"CHN",
            "payerAccount":"123",
            "agreementId":"123",
            "userId":"123",
            "customerId":"123",
            "referenceId":"123",
            "startDateTime":"2010-06-07T15:20:00.325Z",
            "endDateTime":"2010-06-07T15:20:00.325Z",
            "executeDateTime":"2010-06-07T15:20:00.325Z"
        }
    ],
    "code":0
}
```

