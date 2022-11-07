# 根据注册号模糊查找公司信息

## 请求

| Path        | /pdpa/{countryCode}/company/{regestrationNo}/retrieve |
| ----------- | ----------------------------------------------------- |
| Method      | GET                                                   |
| Description | 获取当前国家的PDPA信息                                |

### 请求头

[详见](../header.md)

### 路径参数

| 参数           | 类型   | 必须 | 说明                                                         |
| -------------- | ------ | ---- | ------------------------------------------------------------ |
| countryCode    | string | 是   | 国家代码<br />[详见附录国家代码](../appendices/country_code.md) |
| regestrationNo | string | 是   | 公司注册号                                                   |

## 响应

### 响应体

| 参数             | 类型   | 说明     |
| ---------------- | ------ | -------- |
| companies        | array  | 公司信息 |
| ∟ name           | string | 名称     |
| ∟ regestrationNo | string | 注册号   |
| ∟ address        | string | 地址     |
| ∟ businessType   | string | 业务类型 |
| ∟ capital        | string | 资本金   |
| ∟ shareholders   | array  | 股权人   |
| ∟∟ name          | string | 姓名     |
| ∟∟ NRIC          | string | 身份证件 |
| ∟∟ mobileNumber  | string | 移动电话 |
| ∟∟ email         | string | 电子邮箱 |
| ∟∟ nationality   | string | 国籍     |
| ∟∟ occupation    | string | 职业     |

### 响应体示例

```json
{
  "companies": [
    {
      "name": "company1",
      "regestrationNo": "123456789",
      "address": "address",
      "businessType": "01",
      "capital": "100000000",
      "shareholders": [
        {
          "name": "shareholders1",
          "NRIC": "123456789",
          "mobileNumber": "12455123455",
          "email": "xxxxx@yahoo.com.cn",
          "nationality": "CHN",
          "occupation": "10"
        },
        {
          "name": "shareholders2",
          "NRIC": "123456789",
          "mobileNumber": "12455123455",
          "email": "xxxxx@yahoo.com.cn",
          "nationality": "CHN",
          "occupation": "10"
        },
        {
          "name": "shareholders3",
          "NRIC": "123456789",
          "mobileNumber": "12455123455",
          "email": "xxxxx@yahoo.com.cn",
          "nationality": "CHN",
          "occupation": "10"
        }
      ]
    },
    {
      "name": "company2",
      "regestrationNo": "123456789",
      "address": "address",
      "businessType": "01",
      "capital": "100000000",
      "shareholders": [
        {
          "name": "shareholders1",
          "NRIC": "123456789",
          "mobileNumber": "12455123455",
          "email": "xxxxx@yahoo.com.cn",
          "nationality": "CHN",
          "occupation": "10"
        },
        {
          "name": "shareholders2",
          "NRIC": "123456789",
          "mobileNumber": "12455123455",
          "email": "xxxxx@yahoo.com.cn",
          "nationality": "CHN",
          "occupation": "10"
        },
        {
          "name": "shareholders3",
          "NRIC": "123456789",
          "mobileNumber": "12455123455",
          "email": "xxxxx@yahoo.com.cn",
          "nationality": "CHN",
          "occupation": "10"
        }
      ]
    }
  ]
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |

