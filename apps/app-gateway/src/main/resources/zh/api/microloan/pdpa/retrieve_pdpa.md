# 获取PDPA信息

## 请求

| Path        | /pdpa/{country}/{language}/retrieve |
| ----------- | ----------------------------------- |
| Method      | GET                                 |
| Description | 获取当前国家的PDPA信息              |

### 请求头

[详见](../../header.md)

### 路径参数

| 参数     | 类型   | 必须 | 说明                                                         |
| -------- | ------ | ---- | ------------------------------------------------------------ |
| country  | string | 是   | 国家代码<br />[详见附录国家代码](../../appendices/country_code.md) |
| language | string | 是   | 语言<br />[参见附录字典代码—语言](../../appendices/dictionary_code.md) |

## 响应

### 响应体

| 参数            | 类型   | 说明           |
| --------------- | ------ | -------------- |
| pdpaInformation | array  | 信息要素       |
| ∟ item          | string | 要素大类       |
| ∟ information   | array  | 要素子类列表   |
| ∟∟ label        | string | 要素key        |
| ∟∟ name         | string | 要素名称       |
| id              | string | PDPA协议模板Id |

### 响应体示例

```json
{
  "pdpaInformation": [
      {
          "item":"personalInformation",
          "information":[
              {
                  “label”:"name",
                  "name":"Name"
              },
              {
                  “label”:"aliasName",
                  "name":"Alias Name"
              },
              {
                  “label”:"gender",
                  "name":"Gender"
              },
              {
                  “label”:"dateOfBirth",
                  "name":"Date of Birth"
              }
          ]
      },
      {
          "item":"corporateInformation",
          "information":[
              {
                  “label”:"entityBasicProfile",
                  "name":"Entity Basic Profile"
              },
              {
                  “label”:"entityPreviousNames",
                  "name":"Entity Previous Names"
              },
              {
                  “label”:"entityAddresses",
                  "name":"Entity Addresses"
              }
          ]
      }
  ],
  "id": "123456789"
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息   | 排查建议               |
| ---------- | ------ | ---------- | ---------------------- |
| 404        | 1010   | 租户未找到 | 请求头传入租户是否正确 |
| 404        | 5651   | PDPA未找到 | 排查pdpa是否存在       |