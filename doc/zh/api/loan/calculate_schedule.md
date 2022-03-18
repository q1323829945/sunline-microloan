# 计算还款计划

## 请求

| Path        | /loan/{productId}/{amount}/{term}/calculate |
| ----------- | ------------------------------------------- |
| Method      | GET                                         |
| Description | 获取还款计划表根据金额、期限和贷款产品      |

### 请求头

[详见](../header.md)

### 路径参数

| 参数      | 类型    | 必须 | 说明       |
| --------- | ------- | ---- | ---------- |
| productId | integer | 是   | 贷款产品Id |
| amount    | number  | 是   | 贷款金额   |
| term      | string  | 是   | 期限       |

## 响应

### 响应体

| 参数            | 类型    | 说明                                           |
| --------------- | ------- | ---------------------------------------------- |
| installment     | number  | 期供，每期期供相等的还款该字段有值，如等额本息 |
| interestRate    | number  | 利率                                           |
| schedule        | array   | 还款计划                                       |
| ∟ period        | integer | 期次                                           |
| ∟ repaymentDate | string  | 还款日期                                       |
| ∟ installment   | number  | 期供                                           |
| ∟ principal     | number  | 应还本金                                       |
| ∟ interest      | number  | 应还利息                                       |

### 响应体示例

```json
{
  "installment": 168787.63,
  "interestRate": 4.35,
  "schedule": [
    {
      "period": 1,
      "repaymentDate": "20220116",
      "installment": 168787.63,
      "principal": 165162.63,
      "interest": 3625
    },
    {
      "period": 2,
      "repaymentDate": "20220216",
      "installment": 168787.63,
      "principal": 165761.34,
      "interest": 3026.29
    },
    {
      "period": 3,
      "repaymentDate": "20220316",
      "installment": 168787.63,
      "principal": 166362.23,
      "interest": 2425.4
    }
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

