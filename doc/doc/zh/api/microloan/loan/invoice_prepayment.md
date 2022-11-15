# 提前还款

## 请求

| Path        | /microloan/ConsumerLoan/repay |
| ----------- | ----------------------------- |
| Method      | POST                          |
| Description | 提前还款                      |

### 请求头

[详见](../../header.md)

### 请求体

| 参数               | 类型   | M/O  | 说明                                                         |
| ------------------ | ------ | ---- | ------------------------------------------------------------ |
| agreementId        | string | M    | 协议编号                                                     |
| currency           | string | M    | 币种<br />[详见附件币种代码](../../appendices/currency_code.md) |
| principal          | string | M    | 本金                                                         |
| interest           | string | M    | 利息                                                         |
| fee                | string | M    | 费用                                                         |
| repaymentAccountId | string | M    | 还款账户编号                                                 |
| repaymentAccount   | string | M    | 还款账户                                                     |

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

### 错误码

| HTTP状态码 | 错误码 | 错误信息       | 排查建议               |
| ---------- | ------ | -------------- | ---------------------- |
| 404        | 1010   | 租户未找到     | 请求头传入租户是否正确 |
| 404        | 5560   | 贷款协议未找到 | 查看贷款协议是否存在   |
| 500        | 5614   | 还款指示已存在 | 该笔账单已提前还款     |
| 404        | 1007   | 数据未找到     | 排查还款账户是否存在   |