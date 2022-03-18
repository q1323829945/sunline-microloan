# 获取产品信息

## 请求

| Path        | /product/{identificationCode}/retrieve |
| ----------- | -------------------------------------- |
| Method      | GET                                    |
| Description |                                        |

### 请求头

[详见](../header.md)

### 路径参数

| 参数               | 类型   | 必须 | 说明       |
| ------------------ | ------ | ---- | ---------- |
| identificationCode | string | 是   | 产品标识号 |

## 响应

### 响应体

| 参数                | 类型    | 说明                                                         |
| ------------------- | ------- | ------------------------------------------------------------ |
| productId           | integer | 产品ID,全局唯一                                              |
| identificationCode  | string  | 产品标识号                                                   |
| name                | string  | 产品名称                                                     |
| version             | string  | 版本号                                                       |
| description         | string  | 描述                                                         |
| amountConfiguration | object  | 金额范围参数                                                 |
| ∟ maxValueRange     | number  | 最大金额                                                     |
| ∟ minValueRange     | number  | 最小金额                                                     |
| termConfiguration   | object  | 期限范围参数                                                 |
| ∟ maxValueRange     | string  | 最大期限<br />[参见附录字典代码—期限](../appendices/dictionary_code.md) |
| ∟ minValueRange     | string  | 最小期限<br />[参见附录字典代码—期限](../appendices/dictionary_code.md) |

### 响应体示例

```json
{
        "productId": "99990220099",
        "identificationCode": "SM0010",
        "name": "极速贷",
        "version": "1",
        "description": "提供给中小企业极速办理贷款的感受",
        "amountConfiguration": {
            "maxValueRange": 10000000.00,
            "minValueRange": 10000.00
        },
        "termConfiguration": {
            "maxValueRange": "ONE_YEAR",
            "minValueRange": "THREE_MONTHS"
        }
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

