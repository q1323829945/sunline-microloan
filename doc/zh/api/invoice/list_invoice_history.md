# 查询历史账单

## 请求

| Path        | /invoice/retrieve/{customerId}/history/{invoiceStartDate}/{invoiceEndDate} |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 查询历史账单                                                 |

### 请求头

[详见](../header.md)

### 路径参数

| 参数             | 类型    | M/O  | 说明         |
| ---------------- | ------- | ---- | ------------ |
| customerId       | integer | M    | 客户Id       |
| invoiceStartDate | string  | O    | 账单开始日期 |
| invoiceEndDate   | string  | O    | 账单结束日期 |

## 响应

### 响应体

| 参数                   | 类型    | 说明                                                         |
| ---------------------- | ------- | ------------------------------------------------------------ |
| invoice                | array   | 账单列表                                                     |
| ∟invoicee              | integer | 收账人ID                                                     |
| ∟invoiceId             | integer | 账单ID                                                       |
| ∟invoiceDueDate        | string  | 账单到期日                                                   |
| ∟invoicePeriodFromDate | string  | 账单周期起始日期                                             |
| ∟invoicePeriodToDate   | string  | 账单周期截止日期                                             |
| ∟invoiceTotalAmount    | string  | 账单总金额                                                   |
| ∟invoiceCurrency       | string  | 账单币种<br />[详见附件币种代码](../appendices/currency_code.md) |
| ∟invoiceStatus         | string  | 账单状态<br />[参见附录字典代码—账单状态](../appendices/dictionary_code.md) |
| ∟invoiceLines          | array   | 账单金额明细                                                 |
| ∟∟invoiceAmountType    | string  | 账单金额类型<br />[参见附录字典代码—账单金额类型](../appendices/dictionary_code.md) |
| ∟∟invoiceAmount        | string  | 账单金额                                                     |

### 响应体示例

```json
{
  "invoice": [
    {
      "invoicee": 100000,
  	  "invoiceId": 12030295,
      "invoiceDueDate": "2010-06-07T15:20:00.325Z",
      "invoicePeriodFromDate": "2010-05-07T15:20:00.325Z",
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
    },
    {
      "invoicee": 100000,
      "invoiceId": 120343566,
      "invoiceDueDate": "2010-07-07T15:20:00.325Z",
      "invoicePeriodFromDate": "2010-06-07T15:20:00.325Z",
      "invoicePeriodToDate": "2010-07-07T15:20:00.325Z",
      "invoiceTotalAmount": "1000",
      "invoiceCurrency": "USD",
      "invoiceStatus": "OVERDUE",
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
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

