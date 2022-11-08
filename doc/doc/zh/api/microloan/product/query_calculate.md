# 查询当前产品还款计划

## 请求

| Path        | /microloan/ConsumerLoan/{productId}/{amount}/{term}/calculate |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 查询当前产品还款计划                                         |

### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| productId | string | M    | 产品编号 |
| amount    | string | M    | 金额     |
| term      | string | M    | 期限     |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数            | 类型    | 说明     |
| --------------- | ------- | -------- |
| data            | object  |          |
| ∟installment    | decimal | 分期付款 |
| ∟fee            | decimal | 费用     |
| ∟interestRate   | decimal | 利率     |
| ∟schedule       | array   | 还款计划 |
| ∟∟period        | integer | 期数     |
| ∟∟repaymentDate | string  | 还款日期 |
| ∟∟installment   | decimal | 分期付款 |
| ∟∟principal     | decimal | 本金     |
| ∟∟interest      | decimal | 利息     |
| ∟∟fee           | decimal | 费用     |

### 响应体示例

```json
{
    "data":{
        "installment":5000000,
        "fee":1000,
        "interestRate":0.1,
        "schedule":[
            {
                "period":1,
                "repaymentDate":"2022-10-18T13:25:15.670+08:00",
                "installment":1100010,
                "principal":1000000,
                "interest":100000,
                "fee":10
            },
            {
                "period":2,
                "repaymentDate":"2022-11-18T13:25:15.670+08:00",
                "installment":1100010,
                "principal":1000000,
                "interest":100000,
                "fee":10
            }
        ]
    },
    "code":0
}
```
