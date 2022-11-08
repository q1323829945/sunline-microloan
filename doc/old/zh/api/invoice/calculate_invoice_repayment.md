# 计算账单应还款金额

## 请求

| Path        | /invoice/calculate/{invoiceId}                              |
| ----------- | ----------------------------------------------------------- |
| Method      | GET                                                         |
| Description | 主动偿还逾期账单时计算应还款金额，逾期账单金额包括罚息/费用 |

### 请求头

[详见](../header.md)

### 路径参数

| 参数      | 类型    | M/O  | 说明   |
| --------- | ------- | ---- | ------ |
| invoiceId | integer | M    | 账单Id |

## 响应

### 响应体

| 参数               | 类型    | 说明                                                         |
| ------------------ | ------- | ------------------------------------------------------------ |
| invoicee           | integer | 收账人ID                                                     |
| invoiceId          | integer | 账单ID                                                       |
| invoiceTotalAmount | string  | 账单总金额                                                   |
| invoiceCurrency    | string  | 账单币种<br />[详见附件币种代码](../appendices/currency_code.md) |
| invoiceLines       | array   | 账单金额明细                                                 |
| ∟invoiceAmountType | string  | 账单金额类型<br />[参见附录字典代码—账单金额类型](../appendices/dictionary_code.md) |
| ∟invoiceAmount     | string  | 账单金额                                                     |

### 响应体示例

```json
{
  "invoicee": 100000,
  "invoiceId": 12324456,
  "invoiceTotalAmount": "1010",
  "invoiceCurrency": "USD",
  "invoiceLines": [
    {
      "invoiceAmountType": "PRINCIPAL",
      "invoiceAmount": "950"
    },
    {
      "invoiceAmountType": "INTEREST",
      "invoiceAmount": "50"
    },
    {
      "invoiceAmountType": "FEE",
      "invoiceAmount": "10"
    }
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

