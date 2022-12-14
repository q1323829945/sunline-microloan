# 获取国家信息

## 请求

| Path        | /system/country/{countryCode}/retrieve |
| ----------- | -------------------------------------- |
| Method      | GET                                    |
| Description | 获取当前国家配置信息                   |

### 请求头

[详见](../header.md)

### 路径参数

| 参数        | 类型   | 必须 | 说明                                                         |
| ----------- | ------ | ---- | ------------------------------------------------------------ |
| countryCode | string | 是   | 国家代码<br />[详见附录国家代码](../appendices/country_code.md) |

## 响应

### 响应体

| 参数         | 类型   | 说明         |
| ------------ | ------ | ------------ |
| countryCode  | string | 国家代码     |
| countryName  | string | 国家名称     |
| numberCode   | string | 数字代码     |
| currencyCode | string | 本币         |
| mobileArea   | string | 移动电话区号 |
| datetimeZone | string | 时区         |

### 响应体示例

```json
{
  "countryCode": "SGP",
  "countryName": "Singapore",
  "numberCode": "702",
  "currencyCode": "SGD",
  "mobileArea": "65",
  "datetimeZone":"Asia/Singapore"
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

