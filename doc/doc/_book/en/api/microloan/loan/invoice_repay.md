# Normal repayment

## Request

| Path        | /microloan/ConsumerLoan/invoice/repay |
| ----------- | ------------------------------------- |
| Method      | POST                                  |
| Description | Normal repayment                      |

### Request header

[see](../../header.md)

### Request body

| Parameter          | Type   | M/O  | Description                                                  |
| ------------------ | ------ | ---- | ------------------------------------------------------------ |
| repaymentAccountId | string | M    | Repayment account id                                         |
| repaymentAccount   | string | M    | Repayment account                                            |
| amount             | string | M    | Amount                                                       |
| invoiceId          | string | M    | Invoice id                                                   |
| currency           | string | M    | Currency<br />[See Appendix Currency Code](../../appendices/currency_code.md) |

### Example of request body

```json
{
    "repaymentAccountId":"123",
    "repaymentAccount":"123",
    "amount":"500000",
    "invoiceId":"123",
    "currency":"CHN"
}
```

## Response

### Response body

| Parameter              | Type   | Description                                                  |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | object |                                                              |
| ∟invoicee              | string | Invoicee                                                     |
| ∟invoiceId             | string | Invoice id                                                   |
| ∟invoiceDueDate        | string | Invoice due date                                             |
| ∟invoicePeriodFromDate | string | Invoice period from date                                     |
| ∟invoicePeriodToDate   | string | Invoice period to date                                       |
| ∟invoiceTotalAmount    | string | Invoice total amount                                         |
| ∟invoiceCurrency       | string | currency<br/>[See Appendix Currency Code](../../appendices/currency_code.md) |
| ∟invoiceStatus         | string | Invoice status<br/>[See Appendix Dictionary Code -Invoice status](../../appendices/dictionary_code.md) |
| ∟repaymentStatus       | string | Repayment status<br/>[See Appendix Dictionary Code -Repayment status](../../appendices/dictionary_code.md) |
| ∟agreementId           | string | Agreement id                                                 |
| ∟loanAgreementFromDate | string | Loan agreement from date                                     |
| ∟invoiceRepaymentDate  | string | Invoice repayment date                                       |
| ∟invoiceLines          | array  | Invoice lines                                                |
| ∟∟invoiceAmountType    | string | Invoice amount type<br/>[See Appendix Dictionary Code -Invoice amount type](../../appendices/dictionary_code.md) |
| ∟∟invoiceAmount        | string | Invoice amount                                               |

### Example of response body

```json
{
    "data":{
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
    "code":0
}
```



