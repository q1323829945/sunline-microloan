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
