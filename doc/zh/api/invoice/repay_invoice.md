# 偿还账单

## 请求

| Path        | /invoice/repay |
| ----------- | -------------- |
| Method      | POST           |
| Description | 偿还账单       |

### 请求头

[详见](../header.md)

### 请求体

| 参数      | 类型    | M/O  | 说明   |
| --------- | ------- | ---- | ------ |
| invoiceId | integer | M    | 账单Id |

## 响应

### 响应体

| 参数                  | 类型    | 说明                                                         |
| --------------------- | ------- | ------------------------------------------------------------ |
| invoicee              | integer | 收账人ID                                                     |
| invoiceId             | integer | 账单ID                                                       |
| invoiceDueDate        | string  | 账单到期日                                                   |
| invoicePeriodFromDate | string  | 账单周期起始日期                                             |
| invoicePeriodToDate   | string  | 账单周期截止日期                                             |
| invoiceTotalAmount    | string  | 账单总金额                                                   |
| invoiceCurrency       | string  | 账单币种<br />[详见附件币种代码](../appendices/currency_code.md) |
| invoiceStatus         | string  | 账单状态<br />[参见附录字典代码—账单状态](../appendices/dictionary_code.md) |
| invoiceLines          | array   | 账单金额明细                                                 |
| ∟invoiceAmountType    | string  | 账单金额类型<br />[参见附录字典代码—账单金额类型](../appendices/dictionary_code.md) |
| ∟invoiceAmount        | string  | 账单金额                                                     |

### 响应体示例

```json
{
  "invoicee": 100000,
  "invoiceId": 12030295,
  "invoiceDueDate": "2010-06-07T15:20:00.325Z",
  "invoicePeriodFromDate": "2010-06-07T15:20:00.325Z",
  "invoicePeriodToDate": "2010-06-07T15:20:00.325Z",
  "invoiceTotalAmount": "1000",
  "invoiceCurrency": "USD",  
  "invoiceStatus": "FINISHED",
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
      "invoiceAmount": "0"
    }
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

