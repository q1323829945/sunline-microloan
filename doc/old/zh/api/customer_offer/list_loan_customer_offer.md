# 查询客户贷款申请

## 请求

| Path        | /customerOffer/loan/{customerId}/list |
| ----------- | ------------------------------------- |
| Method      | GET                                   |
| Description | 查询客户贷款申请                      |

### 请求头

[详见](../header.md)

### 路径参数

| 参数       | 类型    | M/O  | 说明   |
| ---------- | ------- | ---- | ------ |
| customerId | integer | M    | 客户Id |

## 响应

### 响应体

| 参数              | 类型    | 说明                                                         |
| ----------------- | ------- | ------------------------------------------------------------ |
| customerOffers    | array   | 客户贷款申请列表                                             |
| ∟ customerOfferId | integer | 申请Id                                                       |
| ∟ amount          | string  | 申请金额                                                     |
| ∟ datetime        | string  | 申请日期                                                     |
| ∟ productName     | string  | 产品名称                                                     |
| ∟ status          | string  | 申请状态<br />[参见附录字典代码—申请状态](../appendices/dictionary_code.md) |

### 响应体示例

```json
{
  "customerOffers": [
    {
      "customerOfferId": 123456789,
      "amount": 6000000,
      "datetime": "2010-06-07T15:20:00.325Z",
      "productName": "Micro Loan",
      "status": "RECORD"
    },
    {
      "customerOfferId": 54323455,
      "amount": "3000000",
      "datetime": "2010-06-07T15:20:00.325Z",
      "productName": "Micro Loan",
      "status": "APPROVALED"
    }
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

