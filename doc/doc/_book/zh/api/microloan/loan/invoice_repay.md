# 期供还款

## 请求

| Path        | /microloan/ConsumerLoan/invoice/repay |
| ----------- | ------------------------------------- |
| Method      | POST                                  |
| Description | 期供还款                              |

### 请求头

[详见](../../header.md)

### 请求体

| 参数               | 类型   | M/O  | 说明                                                         |
| ------------------ | ------ | ---- | ------------------------------------------------------------ |
| repaymentAccountId | string | M    | 还款账户编号                                                 |
| repaymentAccount   | string | M    | 还款账户                                                     |
| amount             | string | M    | 金额                                                         |
| invoiceId          | string | M    | 账单编号                                                     |
| currency           | string | M    | 币种<br />[详见附件币种代码](../../appendices/currency_code.md) |

### 请求体示例

```json
{
    "repaymentAccountId":"123",
    "repaymentAccount":"123",
    "amount":"500000",
    "invoiceId":"123",
    "currency":"CHN"
}
```

## 响应

### 响应体

| 参数                   | 类型   | 说明                                                         |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | object |                                                              |
| ∟invoicee              | string | 账单                                                         |
| ∟invoiceId             | string | 账单编号                                                     |
| ∟invoiceDueDate        | string | 截止日期                                                     |
| ∟invoicePeriodFromDate | string | 周期起始日期                                                 |
| ∟invoicePeriodToDate   | string | 周期截止日期                                                 |
| ∟invoiceTotalAmount    | string | 总金额                                                       |
| ∟invoiceCurrency       | string | 币种<br/>[详见附件币种代码](../../appendices/currency_code.md) |
| ∟invoiceStatus         | string | 账单状态<br/>[参见附录字典代码—账单状态](../../appendices/dictionary_code.md) |
| ∟repaymentStatus       | string | 还款状态<br/>[参见附录字典代码—还款状态](../../appendices/dictionary_code.md) |
| ∟agreementId           | string | 协议编号                                                     |
| ∟loanAgreementFromDate | string | 贷款协议日期                                                 |
| ∟invoiceRepaymentDate  | string | 账单付款日期                                                 |
| ∟invoiceLines          | array  | 账单列表                                                     |
| ∟∟invoiceAmountType    | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](../../appendices/dictionary_code.md) |
| ∟∟invoiceAmount        | string | 账单金额                                                     |

### 

### 响应体示例

### 

```json
{
    "data":{
            "invoicee":"123",
            "invoiceId":"123",
            "invoiceDueDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
            "invoiceTotalAmount":"123",
            "invoiceCurrency":"CHN",
            "invoiceStatus":"ACCOUNTED",
            "repaymentStatus":"UNDO",
            "agreementId":"123",
            "loanAgreementFromDate":"2010-06-07T15:20:00.325Z",
            "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
            "invoiceLines":[
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                },
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                }
            ]
    },
    "code":0
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息       | 排查建议                   |
| ---------- | ------ | -------------- | -------------------------- |
| 404        | 1010   | 租户未找到     | 请求头传入租户是否正确     |
| 404        | 5560   | 贷款协议未找到 | 查看贷款协议是否存在       |
| 500        | 5614   | 还款指示已存在 | 该笔账单已提前还款         |
| 404        | 1007   | 数据未找到     | 排查账单、贷款协议是否存在 |

