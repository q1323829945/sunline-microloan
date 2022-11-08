# 计算贷款应还款金额

## 请求

| Path        | /loan/calculate/{agreementId}/{principal}                 |
| ----------- | --------------------------------------------------------- |
| Method      | GET                                                       |
| Description | 主动偿还贷款时根据要偿还本机计算应还款金额，包括利息/费用 |

### 请求头

[详见](../header.md)

### 路径参数

| 参数        | 类型    | M/O  | 说明     |
| ----------- | ------- | ---- | -------- |
| agreementId | integer | M    | 协议ID   |
| principal   | string  | M    | 偿还本金 |

## 响应

### 响应体

| 参数        | 类型    | 说明                                                         |
| ----------- | ------- | ------------------------------------------------------------ |
| customerId  | integer | 客户ID                                                       |
| agreementId | integer | 协议ID                                                       |
| totalAmount | string  | 总金额                                                       |
| currency    | string  | 币种<br />[详见附件币种代码](../appendices/currency_code.md) |
| items       | array   | 还款金额明细                                                 |
| ∟amountType | string  | 账单金额类型<br />[参见附录字典代码—贷款金额类型](../appendices/dictionary_code.md) |
| ∟amount     | string  | 金额                                                         |

### 响应体示例

```json
{
  "customerId": 100000,
  "agreementId": 12324456,
  "totalAmount": "1010",
  "currency": "USD",
  "items": [
    {
      "amountType": "PRINCIPAL",
      "amount": "950"
    },
    {
      "amountType": "INTEREST",
      "amount": "50"
    },
    {
      "amountType": "FEE",
      "amount": "10"
    }
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

