# 查询还款记录

## 请求

| Path        | /microloan/ConsumerLoan/repayment/record/{customerId}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 查询还款记录                                                 |

### 路径参数

| 参数       | 类型    | M/O  | 说明     |
| ---------- | ------- | ---- | -------- |
| customerId | integer | M    | 用户编号 |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数             | 类型    | 说明                                                         |
| ---------------- | ------- | ------------------------------------------------------------ |
| data             | array   |                                                              |
| ∟id              | string  |                                                              |
| ∟repaymentAmount | decimal | 还款金额                                                     |
| ∟currencyType    | string  | 币种<br/>[详见附件币种代码](../../appendices/currency_code.md) |
| ∟status          | string  | 指令生命周期状态<br/>[参见附录字典代码—指令生命周期状态](../../appendices/dictionary_code.md) |
| ∟payerAccount    | string  | 付款人账户                                                   |
| ∟agreementId     | string  | 协议编号                                                     |
| ∟userId          | string  | 用户编号                                                     |
| ∟customerId      | string  | 客户编号                                                     |
| ∟referenceId     | string  | 关联编号                                                     |
| ∟startDateTime   | string  | 开始日期                                                     |
| ∟endDateTime     | string  | 结束日期                                                     |
| ∟executeDateTime | string  | 执行日期                                                     |

### 响应体示例

```json
{
    "data":[
        {
            "id":"123",
            "repaymentAmount":50000,
            "status":"FULFILLED",
            "currencyType":"CHN",
            "payerAccount":"123",
            "agreementId":"123",
            "userId":"123",
            "customerId":"123",
            "referenceId":"123",
            "startDateTime":"2010-06-07T15:20:00.325Z",
            "endDateTime":"2010-06-07T15:20:00.325Z",
            "executeDateTime":"2010-06-07T15:20:00.325Z"
        },
        {
            "id":"123",
            "repaymentAmount":50000,
            "status":"FULFILLED",
            "currencyType":"CHN",
            "payerAccount":"123",
            "agreementId":"123",
            "userId":"123",
            "customerId":"123",
            "referenceId":"123",
            "startDateTime":"2010-06-07T15:20:00.325Z",
            "endDateTime":"2010-06-07T15:20:00.325Z",
            "executeDateTime":"2010-06-07T15:20:00.325Z"
        }
    ],
    "code":0
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息   | 排查建议               |
| ---------- | ------ | ---------- | ---------------------- |
| 404        | 1010   | 租户未找到 | 请求头传入租户是否正确 |