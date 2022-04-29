# 提前偿还贷款

## 请求

| Path        | /loan/repay  |
| ----------- | ------------ |
| Method      | POST         |
| Description | 主动偿还贷款 |

### 请求头

[详见](../header.md)

### 请求体

| 参数        | 类型    | M/O  | 说明                                                         |
| ----------- | ------- | ---- | ------------------------------------------------------------ |
| agreementId | integer | M    | 协议ID                                                       |
| currency    | string  | M    | 币种<br />[详见附件币种代码](../appendices/currency_code.md) |
| principal   | string  | M    | 偿还本金                                                     |
| interest    | string  | M    | 偿还利息                                                     |
| fee         | string  | M    | 偿还费用                                                     |

## 响应

### 响应体

| 参数 | 类型 | 说明 |
| ---- | ---- | ---- |
|      |      |      |

### 响应体示例

```json
{
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

