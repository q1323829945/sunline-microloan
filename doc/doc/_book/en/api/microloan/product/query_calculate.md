# Query the current product repayment plan

## Request

| Path        | /microloan/ConsumerLoan/{productId}/{amount}/{term}/calculate |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | Query the current product repayment plan                     |

### Path parameter

| Parameter | Type   | M/O  | Description |
| --------- | ------ | ---- | ----------- |
| productId | string | M    | Product id  |
| amount    | string | M    | Amount      |
| term      | string | M    | Term        |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter       | Type    | Description    |
| --------------- | ------- | -------------- |
| data            | object  |                |
| ∟installment    | decimal | Installment    |
| ∟fee            | decimal | Fee            |
| ∟interestRate   | decimal | Interest rate  |
| ∟schedule       | array   | Schedule       |
| ∟∟period        | integer | Period         |
| ∟∟repaymentDate | string  | Repayment date |
| ∟∟installment   | decimal | Installment    |
| ∟∟principal     | decimal | Principal      |
| ∟∟interest      | decimal | Interest       |
| ∟∟fee           | decimal | Fee            |

### Example of response body

```json
{
    "data":{
        "installment":5000000,
        "fee":1000,
        "interestRate":0.1,
        "schedule":[
            {
                "period":1,
                "repaymentDate":"2022-10-18T13:25:15.670+08:00",
                "installment":1100010,
                "principal":1000000,
                "interest":100000,
                "fee":10
            },
            {
                "period":2,
                "repaymentDate":"2022-11-18T13:25:15.670+08:00",
                "installment":1100010,
                "principal":1000000,
                "interest":100000,
                "fee":10
            }
        ]
    },
    "code":0
}
```
