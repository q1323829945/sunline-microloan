# 新建还款账户

## 请求

| Path        | /ConsumerLoan/repaymentAccount |
| ----------- | ------------------------------ |
| Method      | POST                           |
| Description | 新建还款账户                   |

### 请求头

| 参数                   | 类型   | M/O  | 说明     |
| ---------------------- | ------ | ---- | -------- |
| client_id              | string | M    |          |
| access_key             | string | M    |          |
| X-Authorization-Tenant | string | M    | 租户代码 |

## 请求

### 请求体

| 参数                 | 类型    | M/O  | 说明         |
| -------------------- | ------- | ---- | ------------ |
| agreementId          | integer | M    | 申请编号     |
| repaymentAccount     | string  | M    | 还款账户     |
| repaymentAccountBank | string  | M    | 还款账户银行 |
| mobileNumber         | string  | M    | 手机号       |

### 请求体示例

```json
{
    "agreementId":123,
    "repaymentAccount":"123",
    "repaymentAccountBank":"123",
    "mobileNumber":"123"
}
```

## 响应

### 响应体

| 参数                   | 类型   | 说明                                                         |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | object |                                                              |
| ∟agreementId           | string | 协议编号                                                     |
| ∟repaymentAccountLines | array  | 还款账户列表                                                 |
| ∟∟id                   | string |                                                              |
| ∟∟repaymentAccount     | string | 还款账户                                                     |
| ∟∟repaymentAccountBank | string | 还款账户银行                                                 |
| ∟∟status               | string | 状态<br/>[参见附录字典代码—是/否](appendices/dictionary_code.md) |

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
