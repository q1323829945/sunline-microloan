# 账单试算

## 请求

| Path        | /invoice/prepayment/{agreementId}/calculate |
| ----------- | ------------------------------------------- |
| Method      | GET                                         |
| Description | 账单试算                                    |

### 路径参数

| 参数        | 类型    | M/O  | 说明     |
| ----------- | ------- | ---- | -------- |
| agreementId | integer | M    | 协议编号 |

### 请求头

| 参数                   | 类型   | M/O  | 说明     |
| ---------------------- | ------ | ---- | -------- |
| client_id              | string | M    |          |
| access_key             | string | M    |          |
| X-Authorization-Tenant | string | M    | 租户代码 |

## 响应

### 响应体

| 参数                | 类型   | 说明                                                         |
| ------------------- | ------ | ------------------------------------------------------------ |
| data                | object |                                                              |
| ∟agreementId        | string | 协议编号                                                     |
| ∟totalAmount        | string | 总额                                                         |
| ∟prepaymentLines    | array  | 账单列表                                                     |
| ∟∟invoiceAmountType | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](appendices/dictionary_code.md) |
| ∟∟invoiceAmount     | string | 账单金额                                                     |

### 响应体示例

```json
{
    "data":{
        "agreementId":"123",
        "totalAmount":"5000",
        "prepaymentLines":[
            {
                "invoiceAmountType":"PRINCIPAL",
                "invoiceAmount":"4000"
            },
            {
                "invoiceAmountType":"FEE",
                "invoiceAmount":"4000"
            }
        ]
    },
    "code":0
}
```

