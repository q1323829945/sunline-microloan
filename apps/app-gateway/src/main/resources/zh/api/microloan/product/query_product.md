# 查询贷款产品

## 请求

| Path        | /microloan/product |
| ----------- | ------------------ |
| Method      | GET                |
| Description | 查询贷款产品       |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数                 | 类型   | 说明                                                         |
| -------------------- | ------ | ------------------------------------------------------------ |
| data                 | array  |                                                              |
| ∟id                  | string | 产品编号                                                     |
| ∟identificationCode  | string | 产品唯一识别号                                               |
| ∟name                | string | 产品名称                                                     |
| ∟description         | string | 产品描述                                                     |
| ∟loanProductType     | string | 产品类型<br/> [参见附录字典代码—贷款产品类型](../../appendices/dictionary_code.md) |
| ∟loanPurpose         | string | 产品用途                                                     |
| ∟status              | string | 产品状态<br/> [参见附录字典代码—产品状态](../../appendices/dictionary_code.md) |
| ∟amountConfiguration | object | 金额范围                                                     |
| ∟∟id                 | string |                                                              |
| ∟∟maxValueRange      | string | 最大值                                                       |
| ∟∟minValueRange      | string | 最小值                                                       |
| ∟termConfiguration   | object | 期限范围                                                     |
| ∟∟id                 | string |                                                              |
| ∟∟maxValueRange      | string | 最大值<br/> [参见附录字典代码—期限](../../appendices/dictionary_code.md) |
| ∟∟minValueRange      | string | 最小值<br/> [参见附录字典代码—期限](../../appendices/dictionary_code.md) |

### 响应体示例

```json
{
    "data":[
        {
            "id":"95123333",
            "identificationCode":"111222333",
            "name":"产品1号",
            "description":"这是一个产品",
            "loanProductType":"CONSUMER_LOAN",
            "loanPurpose":"用途",
            "status":"SOLD",
            "amountConfiguration":{
                "id":"1",
                "maxValueRange":"5000000",
                "minValueRange":"1000000",
            },
            "termConfiguration":{
                "id":"1",
                "maxValueRange":"THREE_YEAR",
                "minValueRange":"ONE_MONTH",
            }
        },
        {
            "id":"95123333",
            "identificationCode":"111222333",
            "name":"产品1号",
            "description":"这是一个产品",
            "loanProductType":"CONSUMER_LOAN",
            "loanPurpose":"用途",
            "status":"SOLD",
            "amountConfiguration":{
                "id":"1",
                "maxValueRange":"5000000",
                "minValueRange":"1000000",
            },
            "termConfiguration":{
                "id":"1",
                "maxValueRange":"THREE_YEAR",
                "minValueRange":"ONE_MONTH",
            }
        }
    ],
    "code":0
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息   | 排查建议               |
| ---------- | ------ | ---------- | ---------------------- |
| 404        | 1010   | 租户未找到 | 请求头传入租户是否正确 |