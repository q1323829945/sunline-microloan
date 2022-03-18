# 获取PDPA信息

## 请求

| Path        | /pdpa/{countryCode}/retrieve |
| ----------- | ---------------------------- |
| Method      | GET                          |
| Description | 获取当前国家的PDPA信息       |

### 请求头

[详见](../header.md)

### 路径参数

| 参数        | 类型   | 必须 | 说明                                                         |
| ----------- | ------ | ---- | ------------------------------------------------------------ |
| countryCode | string | 是   | 国家代码<br />[详见附录国家代码](../appendices/country_code.md) |

## 响应

### 响应体

| 参数                 | 类型   | 说明           |
| -------------------- | ------ | -------------- |
| personalInformation  | array  | 个人信息要素   |
| ∟ key                | string | 要素key        |
| ∟ name               | string | 要素名称       |
| corporateInformation | array  | 企业信息要素   |
| ∟ key                | string | 要素key        |
| ∟ name               | string | 要素名称       |
| pdpaTemplateId       | string | PDPA协议模板Id |

### 响应体示例

```json
{
  "personalInformation": [
    { "key": "name", "name": "Name" },
    { "key": "aliasName", "name": "Alias Name" },
    { "key": "sex", "name": "Sex" },
    { "key": "dateOfBirth", "name": "Date of Birth" }
  ],
  "corporateInformation": [
    { "key": "entityBasicProfile", "name": "Entity Basic Profile" },
    { "key": "entityPreviousNames", "name": "Entity Previous Names" },
    { "key": "entityAddresses", "name": "Entity Addresses" }
  ],
  "pdpaTemplateId": "123456789"
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

