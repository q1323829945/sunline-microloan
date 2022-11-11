# Query repayment accounts

## Request

| Path        | /microloan/ConsumerLoan/repaymentAccount/{agreementId}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | Query repayment accounts                                     |

### Path parameter

| Parameter   | Type   | M/O  | Description  |
| ----------- | ------ | ---- | ------------ |
| agreementId | string | M    | Agreement id |

### Request header

[see](../../header.md)

## Response

### Response body

| 参数                   | 类型   | 说明                                                         |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | object |                                                              |
| ∟agreementId           | string | Agreement id                                                 |
| ∟repaymentAccountLines | array  | Repayment accounts                                           |
| ∟∟id                   | string |                                                              |
| ∟∟repaymentAccount     | string | Repayment account                                            |
| ∟∟repaymentAccountBank | string | Repayment account bank                                       |
| ∟∟status               | string | status<br/>[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |

### Example of response body

```json
{
    "data":{
        "agreementId":123,
        "repaymentAccountLines":[
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            },
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            }
        ]
    },
    "code":0
}
```

