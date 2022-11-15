# Query periodic supply plan

## Request

| Path        | /microloan/invoice/schedule/{agreementId}/retrieve |
| ----------- | -------------------------------------------------- |
| Method      | GET                                                |
| Description | Query periodic supply plan                         |

### Path parameter

| Parameter   | Type    | M/O  | Description  |
| ----------- | ------- | ---- | ------------ |
| agreementId | integer | M    | Agreement id |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter               | Type    | Description                                                  |
| ----------------------- | ------- | ------------------------------------------------------------ |
| data                    | object  |                                                              |
| ∟agreementId            | string  | Agreement id                                                 |
| ∟repaymentFrequency     | string  | Repayment frequency<br />[See Appendix Dictionary Code -Repayment Frequency Type](../../appendices/dictionary_code.md) |
| ∟repaymentDayType       | string  | Repayment day type<br/>[See Appendix Dictionary Code -Repayment Day Type](../../appendices/dictionary_code.md) |
| ∟paymentMethodType      | string  | Payment method <br/>[See Appendix Dictionary Code -Payment Method Type](../../appendices/dictionary_code.md) |
| ∟fromDate               | string  | From date                                                    |
| ∟endDate                | string  | End date                                                     |
| ∟totalInstalmentLines   | array   | Total instalment lines                                       |
| ∟∟invoiceAmountType     | string  | Invoice amount type<br/>[See Appendix Dictionary Code - Invoice Amount Type](../../appendices/dictionary_code.md) |
| ∟∟invoiceAmount         | string  | Invoice amount                                               |
| ∟scheduleLines          | array   | Schedule lines                                               |
| ∟∟period                | integer | Period                                                       |
| ∟∟invoiceId             | string  | invoice id                                                   |
| ∟∟invoiceInstalment     | string  | Invoice instalment                                           |
| ∟∟invoicePeriodFromDate | string  | Invoice period from date                                     |
| ∟∟invoicePeriodToDate   | string  | Invoice period to date                                       |
| ∟∟invoiceRepaymentDate  | string  | Invoice repayment date                                       |
| ∟∟invoiceLines          | array   | Invoice lines                                                |
| ∟∟∟invoiceAmountType    | string  | Invoice amount type<br/>[See Appendix Dictionary Code - Invoice Amount Type](../../appendices/dictionary_code.md) |
| ∟∟∟invoiceAmount        | string  | Invoice amount                                               |

### Example of response body

```json
{
    "data":{
        "agreementId":"123",
        "repaymentFrequency":"ONE_MONTH",
        "repaymentDayType":"BASE_LOAN_DAY",
        "paymentMethodType":"EQUAL_INSTALLMENT",
        "fromDate":"2010-06-07T15:20:00.325Z",
        "endDate":"2010-06-07T15:20:00.325Z",
        "totalInstalmentLines":[
            {
                "invoiceAmountType":"PRINCIPAL",
                "invoiceAmount":"4000"
            },
            {
                "invoiceAmountType":"FEE",
                "invoiceAmount":"4000"
            }
        ],
        "scheduleLines":[
            {
                "period":1,
                "invoiceId":"123",
                "invoiceInstalment":"123",
                "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
                "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
                "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
                "invoiceLines":[
                    {
                        "invoiceAmountType":"PRINCIPAL",
                        "invoiceAmount":"4000"
                    },
                    {
                        "invoiceAmountType":"FEE",
                        "invoiceAmount":"4000"
                    }
                ]
            }
        ]
    },
    "code":0
}
```

### Error code

| HTTP status code | Error code | Error message                 | Propose                                                      |
| ---------------- | ---------- | ----------------------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant                | Whether the incoming tenant of the request header is correct |
| 404              | 5560       | Invalid loan agreement        | Check whether the loan agreement exists                      |
| 404              | 5601       | Invalid interest rate plan    | Check whether the interest rate plan exists                  |
| 404              | 5610       | Invalid repayment arrangement | Check whether the repayment arrangement exists               |
| 404              | 5620       | Invalid invoice arrangement   | Check whether the invoice arrangement exists                 |