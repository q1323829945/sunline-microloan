# 账单提前还款

## 请求

| Path        | /ConsumerLoan/repay |
| ----------- | ------------------- |
| Method      | POST                |
| Description | 账单提前还款        |

### 请求头

| 参数                   | 类型   | M/O  | 说明     |
| ---------------------- | ------ | ---- | -------- |
| client_id              | string | M    |          |
| access_key             | string | M    |          |
| X-Authorization-Tenant | string | M    | 租户代码 |

## 请求

### 请求体

| 参数               | 类型   | M/O  | 说明                                                      |
| ------------------ | ------ | ---- | --------------------------------------------------------- |
| agreementId        | string | M    | 协议编号                                                  |
| currency           | string | M    | 币种<br />[详见附件币种代码](appendices/currency_code.md) |
| principal          | string | M    | 本金                                                      |
| interest           | string | M    | 利息                                                      |
| fee                | string | M    | 费用                                                      |
| repaymentAccountId | string | M    | 还款账户编号                                              |
| repaymentAccount   | string | M    | 还款账户                                                  |

### 请求体示例

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
