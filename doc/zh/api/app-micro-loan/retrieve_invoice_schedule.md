# 查询还款计划

## 请求

| Path        | /invoice/schedule/{agreementId}/retrieve |
| ----------- | ---------------------------------------- |
| Method      | GET                                      |
| Description | 查询还款计划                             |

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

| 参数                    | 类型    | 说明                                                         |
| ----------------------- | ------- | ------------------------------------------------------------ |
| data                    | object  |                                                              |
| ∟agreementId            | string  | 协议编号                                                     |
| ∟repaymentFrequency     | string  | 还款频率                                                     |
| ∟repaymentDayType       | string  | 还款日类型<br/>[参见附录字典代码—还款日类型](appendices/dictionary_code.md) |
| ∟paymentMethodType      | string  | 还款日期类型<br/>[参见附录字典代码—还款日期类型](appendices/dictionary_code.md) |
| ∟fromDate               | string  | 起始日                                                       |
| ∟endDate                | string  | 截止日                                                       |
| ∟totalInstalmentLines   | array   | 总分期信息                                                   |
| ∟∟invoiceAmountType     | string  | 账单金额类型<br/>[参见附录字典代码—账单金额类型](appendices/dictionary_code.md) |
| ∟∟invoiceAmount         | string  | 账单金额                                                     |
| ∟scheduleLines          | array   | 计划列表                                                     |
| ∟∟period                | integer | 期数                                                         |
| ∟∟invoiceId             | string  | 账单编号                                                     |
| ∟∟invoiceInstalment     | string  | 账单分期                                                     |
| ∟∟invoicePeriodFromDate | string  | 起始日                                                       |
| ∟∟invoicePeriodToDate   | string  | 截止日                                                       |
| ∟∟invoiceRepaymentDate  | string  | 还款日                                                       |
| ∟∟invoiceLines          | array   | 账单信息                                                     |
| ∟∟∟invoiceAmountType    | string  | 账单金额类型<br/>[参见附录字典代码—账单金额类型](appendices/dictionary_code.md) |
| ∟∟∟invoiceAmount        | string  | 账单金额                                                     |

### 响应体示例

```json
{
    "data":{
        "agreementId":"123",
        "repaymentFrequency":"ONE_MONTH",
        "repaymentDayType":"BASE_LOAN_DAY",
        "paymentMethodType":"EQUAL_INSTALLMENT",
        "fromDate":"2010-06-07T15:20:00.325Z",
        "endDate":"2010-06-07T15:20:00.325Z",
        "totalInstalmentLines":[
            {
                "invoiceAmountType":"PRINCIPAL",
                "invoiceAmount":"4000"
            },
            {
                "invoiceAmountType":"FEE",
                "invoiceAmount":"4000"
            }
        ],
        "scheduleLines":[
            {
                "period":1,
                "invoiceId":"123",
                "invoiceInstalment":"123",
                "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
                "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
                "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
                "invoiceLines":[
                    {
                        "invoiceAmountType":"PRINCIPAL",
                        "invoiceAmount":"4000"
                    },
                    {
                        "invoiceAmountType":"FEE",
                        "invoiceAmount":"4000"
                    }
                ]
            }
        ]
    },
    "code":0
}
```

