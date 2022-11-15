# 提前还款试算

## 请求

| Path        | /microloan/ConsumerLoan/prepayment/{agreementId}/calculate |
| ----------- | ---------------------------------------------------------- |
| Method      | GET                                                        |
| Description | 提前还款试算                                               |

### 路径参数

| 参数        | 类型   | M/O  | 说明     |
| ----------- | ------ | ---- | -------- |
| agreementId | string | M    | 申请编号 |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数                | 类型   | 说明                                                         |
| ------------------- | ------ | ------------------------------------------------------------ |
| data                | object |                                                              |
| ∟agreementId        | string | 协议编号                                                     |
| ∟totalAmount        | string | 总额                                                         |
| ∟prepaymentLines    | array  | 账单列表                                                     |
| ∟∟invoiceAmountType | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](../../appendices/dictionary_code.md) |
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

### 错误码

| HTTP状态码 | 错误码 | 错误信息       | 排查建议               |
| ---------- | ------ | -------------- | ---------------------- |
| 404        | 1010   | 租户未找到     | 请求头传入租户是否正确 |
| 404        | 5640   | 还款账户未找到 | 排查还款账户是否存在   |
| 404        | 1007   | 数据未找到     | 排查还款协议是否存在   |