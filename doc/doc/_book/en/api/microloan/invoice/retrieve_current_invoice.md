# Query current invoice

## Request

| Path        | /microloan/invoice/retrieve/{customerId}/current |
| ----------- | ------------------------------------------------ |
| Method      | GET                                              |
| Description | Query current invoice                            |

### Path parameter

| Parameter  | Type    | M/O  | Description |
| ---------- | ------- | ---- | ----------- |
| customerId | integer | M    | Customer id |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter              | Type   | Description                                                  |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | array  |                                                              |
| ∟invoicee              | string | Invoicee                                                     |
| ∟invoiceId             | string | Invoice id                                                   |
| ∟invoiceDueDate        | string | Invoice due date                                             |
| ∟invoicePeriodFromDate | string | Invoice period from date                                     |
| ∟invoicePeriodToDate   | string | Invoice period to date                                       |
| ∟invoiceTotalAmount    | string | Invoice total amount                                         |
| ∟invoiceCurrency       | string | Currency<br/>[See Appendix Currency Code](../../appendices/currency_code.md) |
| ∟invoiceStatus         | string | Invoice status<br/>[See Appendix Dictionary Code -Invoice Status](../../appendices/dictionary_code.md) |
| ∟repaymentStatus       | string | Repayment status<br/>[See Appendix Dictionary Code -Repayment Status](../../appendices/dictionary_code.md) |
| ∟agreementId           | string | Agreement id                                                 |
| ∟loanAgreementFromDate | string | Loan agreement from date                                     |
| ∟invoiceRepaymentDate  | string | Invoice repayment date                                       |
| ∟invoiceLines          | array  | Invoice lines                                                |
| ∟∟invoiceAmountType    | string | Invoice amount type<br/>[See Appendix Dictionary Code -Invoice Amount Type](../../appendices/dictionary_code.md) |
| ∟∟invoiceAmount        | string | Invoice amount                                               |

### Example of response body

```json
{
    "data":[
        {
            "invoicee":"123",
            "invoiceId":"123",
            "invoiceDueDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
            "invoiceTotalAmount":"123",
            "invoiceCurrency":"CHN",
            "invoiceStatus":"ACCOUNTED",
            "repaymentStatus":"UNDO",
            "agreementId":"123",
            "loanAgreementFromDate":"2010-06-07T15:20:00.325Z",
            "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
            "invoiceLines":[
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                },
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                }
            ]
        },
        {   
            "invoicee":"123",
            "invoiceId":"123",
            "invoiceDueDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
            "invoiceTotalAmount":"123",
            "invoiceCurrency":"CHN",
            "invoiceStatus":"ACCOUNTED",
            "repaymentStatus":"UNDO",
            "agreementId":"123",
            "loanAgreementFromDate":"2010-06-07T15:20:00.325Z",
            "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
            "invoiceLines":[
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                },
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                }
            ]
        }
    ],
    "code":0
}
```

### Error code

| HTTP status code | Error code | Error message  | Propose                                                      |
| ---------------- | ---------- | -------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant | Whether the incoming tenant of the request header is correct |
| 404              | 1007       | Invalid data   | Query whether the loan agreement exists                      |