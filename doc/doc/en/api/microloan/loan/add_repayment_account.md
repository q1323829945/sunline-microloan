# Add repayment account

## Request

| Path        | /microloan/ConsumerLoan/repaymentAccount |
| ----------- | ---------------------------------------- |
| Method      | POST                                     |
| Description | Add repayment account                    |

### Request header

[see](../../header.md)

### Request body

| Parameter            | Type    | M/O  | Description            |
| -------------------- | ------- | ---- | ---------------------- |
| agreementId          | integer | M    | Agreement id           |
| repaymentAccount     | string  | M    | Repayment account      |
| repaymentAccountBank | string  | M    | Repayment account Bank |
| mobileNumber         | string  | M    | Mobile                 |

### Example of request body

```json
{
    "agreementId":123,
    "repaymentAccount":"123",
    "repaymentAccountBank":"123",
    "mobileNumber":"123"
}
```

## Response

### Response body

| Parameter              | Type   | Description                                                  |
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
