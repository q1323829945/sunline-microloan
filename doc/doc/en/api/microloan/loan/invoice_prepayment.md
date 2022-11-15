# Prepayment

## Request

| Path        | /microloan/ConsumerLoan/repay |
| ----------- | ----------------------------- |
| Method      | POST                          |
| Description | Prepayment                    |

### Request header

[see](../../header.md)

### Request body

| Parameter          | Type   | M/O  | Description                                                  |
| ------------------ | ------ | ---- | ------------------------------------------------------------ |
| agreementId        | string | M    | Agreement id                                                 |
| currency           | string | M    | Currency Code<br />[See Appendix Currency Code](../../appendices/currency_code.md) |
| principal          | string | M    | Principall                                                   |
| interest           | string | M    | Interest                                                     |
| fee                | string | M    | Fee                                                          |
| repaymentAccountId | string | M    | Repayment account id                                         |
| repaymentAccount   | string | M    | Repayment account                                            |

### Example of request body

```json
{
    "agreementId":"123",
    "currency":"CHN",
    "principal":"5000",
    "interest":"200",
    "fee":"500",
    "repaymentAccountId":"123",
    "repaymentAccount":"123"
}
```

### Error code

| HTTP status code | Error code | Error message                         | Propose                                                      |
| ---------------- | ---------- | ------------------------------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant                        | Whether the incoming tenant of the request header is correct |
| 404              | 5560       | Invalid loan agreement                | Check whether the loan agreement exists                      |
| 500              | 5614       | Repayment instructions already exists | The bill has been prepaid                                    |
| 404              | 1007       | Invalid data                          | Check whether the repayment account exists                   |