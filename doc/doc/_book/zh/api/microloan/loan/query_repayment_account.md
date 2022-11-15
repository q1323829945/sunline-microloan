# 查询还款账户列表

## 请求

| Path        | /microloan/ConsumerLoan/repaymentAccount/{agreementId}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 查询还款账户列表                                             |

### 路径参数

| 参数        | 类型   | M/O  | 说明     |
| ----------- | ------ | ---- | -------- |
| agreementId | string | M    | 申请编号 |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数                   | 类型   | 说明                                                         |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | object |                                                              |
| ∟agreementId           | string | 申请编号                                                     |
| ∟repaymentAccountLines | array  | 还款账户列表                                                 |
| ∟∟id                   | string |                                                              |
| ∟∟repaymentAccount     | string | 还款账户                                                     |
| ∟∟repaymentAccountBank | string | 还款账户银行                                                 |
| ∟∟status               | string | 状态<br/>[参见附录字典代码—是/否](../../appendices/dictionary_code.md) |

### 响应体示例

```json
{
    "data":{
        "agreementId":123,
        "repaymentAccountLines":[
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            },
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
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
| 404        | 5610   | 还款安排未找到 | 排查还款安排是否存在   |