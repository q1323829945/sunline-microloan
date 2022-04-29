# 记录客户贷款申请信息

## 请求

| Path        | /customerOffer/loan/initiate |
| ----------- | ---------------------------- |
| Method      | POST                         |
| Description | 记录客户贷款申请信息         |

### 请求头

| 名称         | 类型   | 必填 | 描述                              |
| ------------ | ------ | ---- | --------------------------------- |
| Content-Type | string | 是   | **固定值**："multipart/form-data" |

[其他属性详见](../header.md)

### 请求体

| 参数                   | 类型    | M/O  | 说明               |
| ---------------------- | ------- | ---- | ------------------ |
| customerOfferProcedure | object  | 是   | 客户申请流程信息   |
| ∟ customerId           | integer | 是   | 客户Id             |
| ∟ customerOfferProcess | string  | 是   | 客户申请处理流程   |
| ∟ employee             | integer | 是   | 处理任务的员工     |
| product                | object  | 是   | 申请的贷款产品信息 |
| ∟ productId            | string  | 是   | 贷款产品Id         |
| pdpa                   | object  | 是   | PDPA信息           |
| ∟pdpaTemplateId        | string  | 是   | PDPA协议模板Id     |
| ∟signature             | png     | 是   | 签名图片           |

### 请求体示例

```json
{
  "customerOfferProcedure": {
      "customerId": "123456789", 
      "customerOfferProcess": "MICRO_LOAN",
      "employee": "654321"
  },
  "product": {
      "productId": "123456789"
  },
  "pdpa": {
      "pdpaTemplateId": "123456789",
      "signature": ""
  }
}
```

## 响应

### 响应体

| 参数                           | 类型    | 说明                                                         |
| ------------------------------ | ------- | ------------------------------------------------------------ |
| customerOfferProcedure         | object  | 客户申请流程信息                                             |
| ∟ customerId                   | integer | 客户Id                                                       |
| ∟ customerOfferProcess         | string  | 客户申请处理流程                                             |
| ∟ employee                     | integer | 处理任务的员工                                               |
| ∟ customerOfferId              | integer | 客户申请Id                                                   |
| ∟ customerOfferProcessNextTask | string  | 客户申请处理流程下一步任务                                   |
| product                        | object  | 产品信息                                                     |
| ∟ productId                    | integer | 产品Id                                                       |
| ∟ amountConfiguration          | object  | 金额范围参数                                                 |
| ∟∟ maxValueRange               | string  | 最大金额                                                     |
| ∟∟ minValueRange               | string  | 最小金额                                                     |
| ∟ termConfiguration            | object  | 期限范围参数                                                 |
| ∟∟ maxValueRange               | string  | 最大期限<br />[参见附录字典代码—期限](../appendices/dictionary_code.md) |
| ∟∟ minValueRange               | string  | 最小期限<br />[参见附录字典代码—期限](../appendices/dictionary_code.md) |

### 响应体示例

```json
{
  "customerOfferProcedure": {
    "customerId": 123456789,
    "customerOfferProcess": "Micro Loan Process",
    "employee": 123455,
    "customerOfferId": 12344566,
    "customerOfferProcessNextTask": "application"
  },
  "product": {
    "productId": 12344,
    "amountConfiguration": {
      "maxValueRange": "1000000000",
      "minValueRange": "10000"
    },
    "termConfiguration": {
      "maxValueRange": "THREE_YEAR",
      "minValueRange": "THREE_MONTHS"
    }
  }
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

