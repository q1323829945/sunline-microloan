# 借款详情

## 请求

| Path        | /microloan/ConsumerLoan/LoanAgreement/detail/{agreementId}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 借款详情                                                     |

### 路径参数

| 参数        | 类型    | M/O  | 说明     |
| ----------- | ------- | ---- | -------- |
| agreementId | integer | M    | 协议编号 |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数                 | 类型   | 说明                                                         |
| -------------------- | ------ | ------------------------------------------------------------ |
| data                 | object |                                                              |
| ∟agreementId         | string | 协议编号                                                     |
| ∟productName         | string | 产品名称                                                     |
| ∟amount              | string | 账单列表                                                     |
| ∟term                | string | 期限<br/>[参见附录字典代码—期限](../../appendices/dictionary_code.md) |
| ∟disbursementAccount | string | 账单金额                                                     |
| ∟purpose             | string | 用途                                                         |
| ∟paymentMethod       | string | 还款方式<br/>[参见附录字典代码—还款方式](../../appendices/dictionary_code.md) |
| ∟disbursementBank    | string | 还款银行                                                     |
| ∟agreementDocumentId | string | 协议文档编号                                                 |

### 响应体示例

```json
{
    "data":{
        "agreementId":"123",
        "productName":"产品1",
        "amount":"500",
        "term":"ONE_MONTH",
        "disbursementAccount":"9000",
        "purpose":"用途",
        "paymentMethod":"EQUAL_INSTALLMENT",
        "disbursementBank":"ABC Bank",
        "agreementDocumentId":"123"
    },
    "code":0
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息       | 排查建议               |
| ---------- | ------ | -------------- | ---------------------- |
| 404        | 1010   | 租户未找到     | 请求头传入租户是否正确 |
| 404        | 5560   | 贷款协议未找到 | 排查贷款协议是否存在   |
| 404        | 5610   | 还款安排未找到 | 排查还款安排是否存在   |
| 404        | 5562   | 支付安排未找到 | 排查支付安排是否存在   |