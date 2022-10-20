[toc]

# 概述

本API文档遵循OpenAPI制定的规范

## 1.Path 模板

Path模板是指使用由大括号 ({}) 分隔的模板表达式，以使用路径参数将 URL 路径的一部分标记为可替换。

路径中的每个模板表达式必须对应一个路径参数，该参数包含在路径项本身和/或每个路径项的操作中。 一个例外是如果路径项为空，例如由于 ACL 约束，则不需要匹配的路径参数。

这些路径参数的值不得包含 [RFC3986] 描述的任何未转义的“通用语法”字符：正斜杠 (/)、问号 (?) 或哈希 (#)。

## 2.版本

版本使用`major`.`minor`.`patch` 版本范式

## 3.原始类型

用于描述json报文字段类型

| 类型    | 说明     |
| ------- | -------- |
| integer | 整型数字 |
| string  | 字符     |
| object  | 对象     |
| array   | 数组     |
| decimal | 浮点数字 |

> 注：非json报文，如上传图片或文件，字段类型另见具体接口说明

## 4.报文示例

接口说明配有报文示例，展示json的报文结构，便于接口测试

> 注：示例数据不能做为测试用例使用

## 5.字段必填项

| 填写类型 | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| M        | 必填                                                         |
| O        | 选填                                                         |
| M?       | 当上级字段为选填时会使用，说明当上级字段填写时，该字段为必填项 |



# 通用请求头

| 参数                   | 类型   | M/O  | 说明                                       |
| ---------------------- | ------ | ---- | ------------------------------------------ |
| client_id              | string | M    |                                            |
| access_key             | string | M    |                                            |
| X-Authorization-Tenant | string | M    | 租户代码                                   |
| Content-Type           | string | M    | **如未特别说明固定值**："application/json" |

# 通用路径

/microloan 

*拼接方式   域名 + /microloan + Path

# 服务响应

## 1.响应状态码

采用HTTP状态码，状态码用于指示执行操作的状态。

| HTTP状态码 | 描述                  | 原因                   |
| ---------- | --------------------- | ---------------------- |
| 200        | Success               |                        |
| 400        | Bad Request           | 因请求的参数校验错误   |
| 401        | Unauthorized          |                        |
| 403        | Forbidden             |                        |
| 404        | Not Found             |                        |
| 429        | Too Many Requests     |                        |
| 500        | Internal Server Error | 服务内部逻辑错误或异常 |



## 2.错误响应体

| 名称              | 类型   | 必填 | 描述     |
| ----------------- | ------ | ---- | -------- |
| status_code       | string | 是   | 错误码   |
| exception_message | string | 是   | 错误信息 |

### 响应体示例

```json
{
  "status_code": "string",
  "exception_message": "string"
}
```

# 请求接口

## 新增客户

### 请求

| Path        | /Customer |
| ----------- | --------- |
| Method      | POST      |
| Description | 新增客户  |

#### 请求头

[见通用请求头](#通用请求头)

#### 请求体

| 参数     | 类型   | M/O  | 说明     |
| -------- | ------ | ---- | -------- |
| userId   | string | M    | 客户编号 |
| username | string | O    | 用户名   |

#### 请求体示例

```json
{
    "username":"张三",
    "userId":"123"
}
```

### 响应

#### 响应体

| 参数      | 类型   | 说明     |
| --------- | ------ | -------- |
| data      | object |          |
| ∟id       | string | 用户编号 |
| ∟username | string | 用户名   |
| ∟userId   | string | 客户编号 |

#### 响应体示例

```json
{
    "data":{
        "id":"123",
        "username":"张三",
        "userId":"123"
    },
    "code":0
}
```

## 查询客户信息

### 请求

| Path        | /Customer/{userId} |
| ----------- | ------------------ |
| Method      | GET                |
| Description | 查询客户信息       |

#### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| client_id | string | M    | 客户编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数      | 类型   | 说明     |
| --------- | ------ | -------- |
| data      | object |          |
| ∟id       | string | 用户编号 |
| ∟username | string | 用户名   |
| ∟userId   | string | 客户编号 |

#### 响应体示例

```json
{
    "data":{
        "id":"123",
        "username":"张三",
        "userId":"123"
    },
    "code":0
}
```

## 查询贷款产品

### 请求

| Path        | /product     |
| ----------- | ------------ |
| Method      | GET          |
| Description | 查询贷款产品 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                 | 类型   | 说明                                                     |
| -------------------- | ------ | -------------------------------------------------------- |
| data                 | array  |                                                          |
| ∟id                  | string | 产品编号                                                 |
| ∟identificationCode  | string | 产品唯一识别号                                           |
| ∟name                | string | 产品名称                                                 |
| ∟description         | string | 产品描述                                                 |
| ∟loanProductType     | string | 产品类型<br/> [参见附录字典代码—贷款产品类型](#字典代码) |
| ∟loanPurpose         | string | 产品用途                                                 |
| ∟status              | string | 产品状态<br/> [参见附录字典代码—产品状态](#字典代码)     |
| ∟amountConfiguration | object | 金额范围                                                 |
| ∟∟id                 | string |                                                          |
| ∟∟maxValueRange      | string | 最大值                                                   |
| ∟∟minValueRange      | string | 最小值                                                   |
| ∟termConfiguration   | object | 期限范围                                                 |
| ∟∟id                 | string |                                                          |
| ∟∟maxValueRange      | string | 最大值<br/> [参见附录字典代码—期限](#字典代码)           |
| ∟∟minValueRange      | string | 最小值<br/> [参见附录字典代码—期限](#字典代码)           |

#### 响应体示例

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

## 查询贷款产品期限参数

### 请求

| Path        | /product/interestRate/{productId} |
| ----------- | --------------------------------- |
| Method      | GET                               |
| Description | 查询贷款产品期限参数              |

#### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| productId | string | M    | 产品编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数 | 类型  | 说明 |
| ---- | ----- | ---- |
| data | array |      |

#### 响应体示例

```json
{
    "data":[
		"ONE_MONTH","THREE_MONTHS","SIX_MONTHS"
    ],
    "code":0
}
```

## 查询上传模版

### 请求

| Path        | /CustomerOffer/loanUploadTemplate/{productId} |
| ----------- | --------------------------------------------- |
| Method      | GET                                           |
| Description | 查询上传模版                                  |

#### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| productId | string | M    | 产品编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 数    | 类型   | 说明     |
| ----- | ------ | -------- |
| data  | array  |          |
| ∟id   | string | 模版编号 |
| ∟name | string | 模版名称 |

#### 响应体示例

```json
{
    "data":[
        {
            "id":"1",
            "name":"证件"
        },
        {
            "id":"2",
            "name":"流水"
        }
    ],
    "code":0
}
```



## 查询还款计划

### 请求

| Path        | /ConsumerLoan/{productId}/{amount}/{term}/calculate |
| ----------- | --------------------------------------------------- |
| Method      | GET                                                 |
| Description | 查询还款计划                                        |

#### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| productId | string | M    | 产品编号 |
| amount    | string | M    | 金额     |
| term      | string | M    | 期限     |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

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

#### 响应体示例

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

## 记录客户贷款申请信息

### 请求

| Path        | /customerOffer/loan/initiate |
| ----------- | ---------------------------- |
| Method      | POST                         |
| Description | 记录客户贷款申请信息         |

#### 请求头

| 参数         | 类型   | M/O  | 说明                              |
| ------------ | ------ | ---- | --------------------------------- |
| Content-Type | string | M    | **固定值**："multipart/form-data" |

[其他见通用请求头](#通用请求头)

#### 请求体

| 参数                   | 类型    | M/O  | 说明               |
| ---------------------- | ------- | ---- | ------------------ |
| customerOfferProcedure | object  | M    | 客户申请流程信息   |
| ∟ customerId           | integer | M    | 客户Id             |
| ∟ customerOfferProcess | string  | M    | 客户申请处理流程   |
| ∟ employee             | integer | M    | 处理任务的员工     |
| product                | object  | M    | 申请的贷款产品信息 |
| ∟ productId            | string  | M    | 贷款产品Id         |
| ∟productName           | string  | M    | 产品名称           |
| pdpa                   | object  | M    | PDPA信息           |
| ∟pdpaTemplateId        | string  | M    | PDPA协议模板Id     |
| ∟signature             | string  | O    | 签名图片名称       |

#### 请求体示例

```json
key:customerOffer
value:
{
  "customerOfferProcedure": {
      "customerId": "123456789", 
      "customerOfferProcess": "MICRO_LOAN",
      "employee": "654321"
  },
  "product": {
      "productId": "123456789",
      "productName":"asdasd",
  },
  "pdpa": {
      "pdpaTemplateId": "123456789",
      "signature": ""
  }
}
contentType: application/json
```

```json
key:  signature
value:  file
contentType:  multipart/form-data
```

### 响应

#### 响应体

| 参数                           | 类型    | 说明                                             |
| ------------------------------ | ------- | ------------------------------------------------ |
| customerOfferProcedure         | object  | 客户申请流程信息                                 |
| ∟ customerId                   | integer | 客户Id                                           |
| ∟ customerOfferProcess         | string  | 客户申请处理流程                                 |
| ∟ employee                     | integer | 处理任务的员工                                   |
| ∟ customerOfferId              | integer | 客户申请Id                                       |
| ∟ customerOfferProcessNextTask | string  | 客户申请处理流程下一步任务                       |
| product                        | object  | 产品信息                                         |
| ∟ productId                    | integer | 产品Id                                           |
| ∟ amountConfiguration          | object  | 金额范围参数                                     |
| ∟∟ maxValueRange               | string  | 最大金额                                         |
| ∟∟ minValueRange               | string  | 最小金额                                         |
| ∟ termConfiguration            | object  | 期限范围参数                                     |
| ∟∟ maxValueRange               | string  | 最大期限<br />[参见附录字典代码—期限](#字典代码) |
| ∟∟ minValueRange               | string  | 最小期限<br />[参见附录字典代码—期限](#字典代码) |

#### 响应体示例

```json
{
  "customerOfferProcedure": {
    "customerId": 123456789,
    "customerOfferProcess": "Micro Loan Process",
    "employee": 123455,
    "customerOfferId": 12344566,
    "customerOfferProcessNextTask": "application"
  },
  "product": {
    "productId": 12344,
    "amountConfiguration": {
      "maxValueRange": "1000000000",
      "minValueRange": "10000"
    },
    "termConfiguration": {
      "maxValueRange": "THREE_YEAR",
      "minValueRange": "THREE_MONTHS"
    }
  }
}
```

## 提交客户贷款申请

### 请求

| Path        | /customerOffer/loan/{customerOfferId}/submit |
| ----------- | -------------------------------------------- |
| Method      | PUT                                          |
| Description | 提交客户贷款申请信息                         |

#### 请求头

| 参数         | 类型   | M/O  | 说明                              |
| ------------ | ------ | ---- | --------------------------------- |
| Content-Type | string | M    | **固定值**："multipart/form-data" |

[其他见通用请求头](#通用请求头)

#### 路径参数

| 参数            | 类型    | M/O  | 描述       |
| --------------- | ------- | ---- | ---------- |
| customerOfferId | integer | M    | 客户申请Id |

#### 请求体

| 参数                            | 类型    | M/O  | 描述                                                         |
| ------------------------------- | ------- | ---- | ------------------------------------------------------------ |
| loan                            | object  | M    | 贷款申请信息                                                 |
| ∟ amount                        | string  | M    | 申请金额                                                     |
| ∟ currency                      | string  | M    | 币种代码，默认本币<br />[详见附件币种代码](#币种代码)        |
| ∟ term                          | string  | M    | 期限<br />[参见附录字典代码—期限](#字典代码)                 |
| ∟ local                         | string  | M    | 业务经营地区是否为本地<br />[参见附录字典代码—是/否](#字典代码) |
| ∟ employ                        | string  | M    | 有多少员工，选项：1-10~30；2-30~50；3-50~100；4-100以上      |
| company                         | object  | M    | 公司信息                                                     |
| ∟ regestrationNo                | string  | M    | 注册号                                                       |
| contact                         | object  | M    | 联系人信息                                                   |
| ∟contacts                       | string  | M    | 联系人                                                       |
| ∟ contactNRIC                   | string  | M    | 联系人身份证                                                 |
| ∟ mobileArea                    | string  | M    | 移动电话区域码                                               |
| ∟ mobileNumber                  | string  | M    | 移动号码                                                     |
| ∟ email                         | string  | M    | 电子邮件地址                                                 |
| detail                          | object  | M    | 公司详细信息                                                 |
| ∟ name                          | string  | M    | 注册名称                                                     |
| ∟ regestrationNo                | string  | M    | 注册号                                                       |
| ∟ address                       | string  | M    | 注册地址                                                     |
| ∟ businessType                  | string  | M    | 业务类型<br />TODO                                           |
| ∟ contactAddress                | string  | M    | 联系地址                                                     |
| ∟ businessPremiseType           | string  | M    | 经营场所类型<br />[参见附录字典代码—所有权类型](#字典代码)   |
| ∟ businessFocused               | integer | M    | 业务在制造业和服务业的关注度，数值越小越关注于制造业，数值越大越关注于服务业 |
| guarantor                       | object  | M    | 担保人信息                                                   |
| ∟ primaryGuarantor              | string  | M    | 主担保人的身份证                                             |
| ∟ guarantors                    | array   | M    | 担保人列表                                                   |
| ∟∟ name                         | string  | M    | 姓名                                                         |
| ∟∟ nric                         | string  | M    | 身份证                                                       |
| ∟∟ nationality                  | string  | M    | 国家代码<br />[详见附录国家代码](#国家代码)                  |
| ∟∟ mobileArea                   | string  | M    | 移动电话区域码                                               |
| ∟∟ mobileNumber                 | string  | M    | 移动号码                                                     |
| ∟∟ email                        | string  | M    | 电子邮件地址                                                 |
| ∟∟ occupation                   | string  | M    | 职业<br />TODO                                               |
| ∟∟industryExpYear               | integer | M    | 行业经验年限                                                 |
| ∟∟manageExpYear                 | integer | M    | 管理经验年限                                                 |
| ∟∟ residenceType                | string  | M    | 住宅类型<br />TODO                                           |
| ∟∟ residenceOwnership           | string  | M    | 住宅所有权<br />[参见附录字典代码—所有权类型](#字典代码)     |
| financial                       | object  | M    | 公司财务信息                                                 |
| ∟ lastestYearRevenus            | string  | M    | 可选：1-小于1千万；2-1千万到1亿；3-大于1亿                   |
| ∟ mainAccountWithOurBank        | string  | M    | 公司主账户是否在我行<br />[参见附录字典代码—是/否](#字典代码) |
| ∟ outLoanNotWithOutBank         | string  | M    | 是否有不在我行的外部贷款<br />[参见附录字典代码—是/否](#字典代码) |
| uploadDocument                  | array   | O    | 上传文件                                                     |
| ∟ documentTemplateId            | integer | M?   | 文档模板Id                                                   |
| ∟ file                          | string  | M?   | 文件名 格式：documentTemplateId/fileName                     |
| kyc                             | object  | M    | kyc相关信息                                                  |
| ∟businessInBlackListArea        | string  | M    | 是否在黑名单地区有业务<br />[参见附录字典代码—是/否](#字典代码) |
| ∟businessPlanInBlackListArea    | string  | M    | 是否在黑名单地区有开展业务规划<br />[参见附录字典代码—是/否](#字典代码) |
| ∟businessOrPartnerSanctioned    | string  | M    | 业务或合作伙伴是否被制裁<br />[参见附录字典代码—是/否](#字典代码) |
| ∟relationInBlackListArea        | string  | M    | 业务关联方是否在黑名单地区<br />[参见附录字典代码—是/否](#字典代码) |
| ∟repaymentSourceInBlackListArea | string  | M    | 贷款还款来源关联方是否在黑名单地区<br />[参见附录字典代码—是/否](#字典代码) |
| ∟representsNeutrality           | string  | M    | 业务是否与第三方无关<br />[参见附录字典代码—是/否](#字典代码) |
| ∟representsNeutralityShared     | string  | M    | 业务是否与第三方共享相关属性，地址、电话号码、受益所有人、授权签字人、员工<br />[参见附录字典代码—是/否](#字典代码) |
| ∟familiarWithBusiness           | string  | M    | 业务范围类型的企业，选项：1-化学物质批发商；2-国防相关业务；3-大使馆、领事馆和高级专员公署客户；4-能源和金属采掘业；5-从事批发银行券业务的金融机构；6-持牌赌场、博彩企业和持牌赌场中介；7-持牌放债人；8-货币兑换商和汇款代理商；9-石油和天然气(包括加油)；10-支付服务提供商；11-商品的实物运输；12-纯私人银行；13-航运、海运或船舶租赁(包括加油)；14-没有核心经营业务或基础业务交易的特殊目的实体 |

#### 请求体示例

```json
key: customer
value: 
{
      "loan": {
        "amount": "1000000",
        "currency": "SGP",
        "term": "SIX_MONTHS",
        "local": "Y",
        "employ": "1"
      },
      "company": {
        "regestrationNo": "000666303"
      },
      "contact": {
        "contants":"张三",
        "contactNRIC": "123456789",
        "mobileArea": "86",
        "mobileNumber": "1765498274",
        "email": "xxxx@hotmail.cn"
      },
      "detail": {
        "name": "xxxx company",
        "regestrationNo": "000666303",
        "address": "xxxx street",
        "businessType": "1",
        "contactAddress": "xxxx road",
        "businessPremiseType": "OWNED",
        "businessFocused": 40
      },
      "guarantor": {
        "primaryGuarantor": "123445",
        "guarantors": [
          {
            "name": "Coco",
            "nric": "123445",
            "nationality": "USA",
            "mobileArea": "65",
            "mobileNumber": "1238765447",
            "email": "xxxx@hotmail.cn",
            "occupation": "1",
            "industryExpYear": 6,
            "manageExpYear": 3,
            "residenceType": "1",
            "residenceOwnership": "RENTED"
          },
          {
            "name": "Lili",
            "nric": "123445",
            "nationality": "USA",
            "mobileArea": "65",
            "mobileNumber": "1238765447",
            "email": "xxxx@hotmail.cn",
            "occupation": "1",
            "industryExpYear": 6,
            "manageExpYear": 3,
            "residenceType": "1",
            "residenceOwnership": "RENTED"
          }
        ]
      },
      "financial": {
        "lastestYearRevenus": "1",
        "mainAccountWithOurBank": "Y",
        "outLoanNotWithOutBank": "N"
      },
      "uploadDocument": [
        { "documentTemplateId": 123455312, "file": "123455312/weofowusfljwoeuf.png" },
        { "documentTemplateId": 123453577, "file": "123453577/weofowusfljwoeuf.pig" }
      ],
      "kyc": {
        "businessInBlackListArea": "N",
        "businessPlanInBlackListArea": "N",
        "businessOrPartnerSanctioned": "N",
        "relationInBlackListArea": "N",
        "repaymentSourceInBlackListArea": "N",
        "representsNeutrality": "N",
        "representsNeutralityShared": "N",
        "familiarWithBusiness": "14"
      }
}

contentType: application:json
```

```
key: files --可多个
value: file 文件命名规则： documentTemplateId/fileName
contentType: multipart/form-data
```

## 修改客户贷款申请

### 请求

| Path        | /customerOffer/loan/{customerOfferId}/update |
| ----------- | -------------------------------------------- |
| Method      | PUT                                          |
| Description | 修改客户贷款申请信息                         |

#### 请求头

| 参数         | 类型   | M/O  | 说明                              |
| ------------ | ------ | ---- | --------------------------------- |
| Content-Type | string | M    | **固定值**："multipart/form-data" |

[其他见通用请求头](#通用请求头)

#### 路径参数

| 参数            | 类型    | M/O  | 描述       |
| --------------- | ------- | ---- | ---------- |
| customerOfferId | integer | M    | 客户申请Id |

#### 请求体

| 参数                            | 类型    | M/O  | 描述                                                         |
| ------------------------------- | ------- | ---- | ------------------------------------------------------------ |
| loan                            | object  | M    | 贷款申请信息                                                 |
| ∟ amount                        | string  | M    | 申请金额                                                     |
| ∟ currency                      | string  | M    | 币种代码，默认本币<br />[详见附件币种代码](#币种代码)        |
| ∟ term                          | string  | M    | 期限<br />[参见附录字典代码—期限](#字典代码)                 |
| ∟ local                         | string  | M    | 业务经营地区是否为本地<br />[参见附录字典代码—是/否](#字典代码) |
| ∟ employ                        | string  | M    | 有多少员工，选项：1-10~30；2-30~50；3-50~100；4-100以上      |
| company                         | object  | M    | 公司信息                                                     |
| ∟ regestrationNo                | string  | M    | 注册号                                                       |
| contact                         | object  | M    | 联系人信息                                                   |
| ∟contacts                       | string  | M    | 联系人                                                       |
| ∟ contactNRIC                   | string  | M    | 联系人身份证                                                 |
| ∟ mobileArea                    | string  | M    | 移动电话区域码                                               |
| ∟ mobileNumber                  | string  | M    | 移动号码                                                     |
| ∟ email                         | string  | M    | 电子邮件地址                                                 |
| detail                          | object  | M    | 公司详细信息                                                 |
| ∟ name                          | string  | M    | 注册名称                                                     |
| ∟ regestrationNo                | string  | M    | 注册号                                                       |
| ∟ address                       | string  | M    | 注册地址                                                     |
| ∟ businessType                  | string  | M    | 业务类型<br />TODO                                           |
| ∟ contactAddress                | string  | M    | 联系地址                                                     |
| ∟ businessPremiseType           | string  | M    | 经营场所类型<br />[参见附录字典代码—所有权类型](#字典代码)   |
| ∟ businessFocused               | integer | M    | 业务在制造业和服务业的关注度，数值越小越关注于制造业，数值越大越关注于服务业 |
| guarantor                       | object  | M    | 担保人信息                                                   |
| ∟ primaryGuarantor              | string  | M    | 主担保人的身份证                                             |
| ∟ guarantors                    | array   | M    | 担保人列表                                                   |
| ∟∟ name                         | string  | M    | 姓名                                                         |
| ∟∟ nric                         | string  | M    | 身份证                                                       |
| ∟∟ nationality                  | string  | M    | 国家代码<br />[详见附录国家代码](#国家代码)                  |
| ∟∟ mobileArea                   | string  | M    | 移动电话区域码                                               |
| ∟∟ mobileNumber                 | string  | M    | 移动号码                                                     |
| ∟∟ email                        | string  | M    | 电子邮件地址                                                 |
| ∟∟ occupation                   | string  | M    | 职业<br />TODO                                               |
| ∟∟industryExpYear               | integer | M    | 行业经验年限                                                 |
| ∟∟manageExpYear                 | integer | M    | 管理经验年限                                                 |
| ∟∟ residenceType                | string  | M    | 住宅类型<br />TODO                                           |
| ∟∟ residenceOwnership           | string  | M    | 住宅所有权<br />[参见附录字典代码—所有权类型](#字典代码)     |
| financial                       | object  | M    | 公司财务信息                                                 |
| ∟ lastestYearRevenus            | string  | M    | 可选：1-小于1千万；2-1千万到1亿；3-大于1亿                   |
| ∟ mainAccountWithOurBank        | string  | M    | 公司主账户是否在我行<br />[参见附录字典代码—是/否](#字典代码) |
| ∟ outLoanNotWithOutBank         | string  | M    | 是否有不在我行的外部贷款<br />[参见附录字典代码—是/否](#字典代码) |
| uploadDocument                  | array   | O    | 上传文件                                                     |
| ∟ documentTemplateId            | integer | M?   | 文档模板Id                                                   |
| ∟ file                          | string  | M?   | 文件名 格式：documentTemplateId/fileName                     |
| kyc                             | object  | M    | kyc相关信息                                                  |
| ∟businessInBlackListArea        | string  | M    | 是否在黑名单地区有业务<br />[参见附录字典代码—是/否](#字典代码) |
| ∟businessPlanInBlackListArea    | string  | M    | 是否在黑名单地区有开展业务规划<br />[参见附录字典代码—是/否](#字典代码) |
| ∟businessOrPartnerSanctioned    | string  | M    | 业务或合作伙伴是否被制裁<br />[参见附录字典代码—是/否](#字典代码) |
| ∟relationInBlackListArea        | string  | M    | 业务关联方是否在黑名单地区<br />[参见附录字典代码—是/否](#字典代码) |
| ∟repaymentSourceInBlackListArea | string  | M    | 贷款还款来源关联方是否在黑名单地区<br />[参见附录字典代码—是/否](#字典代码) |
| ∟representsNeutrality           | string  | M    | 业务是否与第三方无关<br />[参见附录字典代码—是/否](#字典代码) |
| ∟representsNeutralityShared     | string  | M    | 业务是否与第三方共享相关属性，地址、电话号码、受益所有人、授权签字人、员工<br />[参见附录字典代码—是/否](#字典代码) |
| ∟familiarWithBusiness           | string  | M    | 业务范围类型的企业，选项：1-化学物质批发商；2-国防相关业务；3-大使馆、领事馆和高级专员公署客户；4-能源和金属采掘业；5-从事批发银行券业务的金融机构；6-持牌赌场、博彩企业和持牌赌场中介；7-持牌放债人；8-货币兑换商和汇款代理商；9-石油和天然气(包括加油)；10-支付服务提供商；11-商品的实物运输；12-纯私人银行；13-航运、海运或船舶租赁(包括加油)；14-没有核心经营业务或基础业务交易的特殊目的实体 |

#### 请求体示例

```json
key: customer
value: 
{
      "loan": {
        "amount": "1000000",
        "currency": "SGP",
        "term": "SIX_MONTHS",
        "local": "Y",
        "employ": "1"
      },
      "company": {
        "regestrationNo": "000666303"
      },
      "contact": {
        "contants":"张三",
        "contactNRIC": "123456789",
        "mobileArea": "86",
        "mobileNumber": "1765498274",
        "email": "xxxx@hotmail.cn"
      },
      "detail": {
        "name": "xxxx company",
        "regestrationNo": "000666303",
        "address": "xxxx street",
        "businessType": "1",
        "contactAddress": "xxxx road",
        "businessPremiseType": "OWNED",
        "businessFocused": 40
      },
      "guarantor": {
        "primaryGuarantor": "123445",
        "guarantors": [
          {
            "name": "Coco",
            "nric": "123445",
            "nationality": "USA",
            "mobileArea": "65",
            "mobileNumber": "1238765447",
            "email": "xxxx@hotmail.cn",
            "occupation": "1",
            "industryExpYear": 6,
            "manageExpYear": 3,
            "residenceType": "1",
            "residenceOwnership": "RENTED"
          },
          {
            "name": "Lili",
            "nric": "123445",
            "nationality": "USA",
            "mobileArea": "65",
            "mobileNumber": "1238765447",
            "email": "xxxx@hotmail.cn",
            "occupation": "1",
            "industryExpYear": 6,
            "manageExpYear": 3,
            "residenceType": "1",
            "residenceOwnership": "RENTED"
          }
        ]
      },
      "financial": {
        "lastestYearRevenus": "1",
        "mainAccountWithOurBank": "Y",
        "outLoanNotWithOutBank": "N"
      },
      "uploadDocument": [
        { "documentTemplateId": 123455312, "file": "123455312/weofowusfljwoeuf.png" },
        { "documentTemplateId": 123453577, "file": "123453577/weofowusfljwoeuf.pig" }
      ],
      "kyc": {
        "businessInBlackListArea": "N",
        "businessPlanInBlackListArea": "N",
        "businessOrPartnerSanctioned": "N",
        "relationInBlackListArea": "N",
        "repaymentSourceInBlackListArea": "N",
        "representsNeutrality": "N",
        "representsNeutralityShared": "N",
        "familiarWithBusiness": "14"
      }
}

contentType: application:json
```

```
key: files --可多个
value: file 文件命名规则： documentTemplateId/fileName
contentType: multipart/form-data
```

## 检索客户贷款申请信息

### 请求

| Path        | /customerOffer/loan/{customerOfferId}/{countryCode}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 检索客户贷款申请信息                                         |

#### 请求头

[见通用请求头](#通用请求头)

#### 路径参数

| 参数            | 类型    | M/O  | 说明                                                         |
| --------------- | ------- | ---- | ------------------------------------------------------------ |
| customerOfferId | integer | M    | 客户申请Id                                                   |
| countryCode     | string  | M    | 国家代码<br />[详见附录国家代码](appendices/country_code.md) |

### 响应

#### 响应体

| 参数                             | 类型    | 说明                                                         |
| -------------------------------- | ------- | ------------------------------------------------------------ |
| data                             | object  |                                                              |
| ∟customerOfferProcedure          | object  | 客户申请流程信息                                             |
| ∟∟ customerOfferId               | integer | 申请Id                                                       |
| ∟∟ customerId                    | integer | 客户Id                                                       |
| ∟∟ customerOfferProcess          | string  | 客户申请处理流程<br />TODO                                   |
| ∟∟ employee                      | integer | 处理任务的员工                                               |
| ∟∟ customerOfferProcessNextTask  | string  | 下一步任务<br />TODO                                         |
| ∟∟ status                        | string  | 申请状态<br />[参见附录字典代码—申请状态](#字典代码)         |
| ∟pdpa                            | object  | pdpa信息                                                     |
| ∟∟id                             | string  |                                                              |
| ∟∟pdpaInformation                | array   | 个人信息要素                                                 |
| ∟∟∟ item                         | string  | 要素item                                                     |
| ∟∟∟ information                  | array   |                                                              |
| ∟∟∟∟label                        | string  | 要素key                                                      |
| ∟∟∟∟name                         | string  | 要素名称                                                     |
| ∟product                         | object  | 产品信息                                                     |
| ∟∟productId                      | integer | 产品ID,全局唯一                                              |
| ∟∟identificationCode             | string  | 产品标识号                                                   |
| ∟∟name                           | string  | 产品名称                                                     |
| ∟∟version                        | string  | 版本号                                                       |
| ∟∟description                    | string  | 描述                                                         |
| ∟∟amountConfiguration            | object  | 金额范围参数                                                 |
| ∟∟∟ maxValueRange                | string  | 最大金额                                                     |
| ∟∟∟ minValueRange                | string  | 最小金额                                                     |
| ∟∟termConfiguration              | object  | 期限范围参数                                                 |
| ∟∟∟ maxValueRange                | string  | 最大期限<br />[参见附录字典代码—期限](#字典代码)             |
| ∟∟∟ minValueRange                | string  | 最小期限<br />[参见附录字典代码—期限](#字典代码)             |
| ∟loan                            | object  | 贷款申请信息                                                 |
| ∟∟ amount                        | string  | 申请金额                                                     |
| ∟∟ currency                      | string  | 币种代码，默认本币<br />[详见附件币种代码](#币种代码)        |
| ∟∟ term                          | string  | 期限<br />[参见附录字典代码—期限](#字典代码)                 |
| ∟∟ local                         | string  | 业务经营地区是否为本地<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟ employ                        | string  | 有多少员工，选项：1-10~30；2-30~50；3-50~100；4-100以上      |
| ∟company                         | object  | 公司信息                                                     |
| ∟∟ regestrationNo                | string  | 注册号                                                       |
| ∟contact                         | object  | 联系人信息                                                   |
| ∟∟contacts                       | string  | 联系人                                                       |
| ∟∟ contactNRIC                   | string  | 联系人身份证                                                 |
| ∟∟ mobileArea                    | string  | 移动电话区域码                                               |
| ∟∟ mobileNumber                  | string  | 移动号码                                                     |
| ∟∟ email                         | string  | 电子邮件地址                                                 |
| ∟detail                          | object  | 公司详细信息                                                 |
| ∟∟ name                          | string  | 注册名称                                                     |
| ∟∟ regestrationNo                | string  | 注册号                                                       |
| ∟∟ address                       | string  | 注册地址                                                     |
| ∟∟ businessType                  | string  | 业务类型<br />TODO                                           |
| ∟∟ contactAddress                | string  | 联系地址                                                     |
| ∟∟ businessPremiseType           | string  | 经营场所类型<br />[参见附录字典代码—所有权类型](#字典代码)   |
| ∟∟ businessFocused               | integer | 业务在制造业和服务业的关注度，数值越小越关注于制造业，数值越大越关注于服务业 |
| ∟guarantor                       | object  | 担保人信息                                                   |
| ∟∟ primaryGuarantor              | string  | 主担保人的身份证                                             |
| ∟∟ guarantors                    | array   | 担保人列表                                                   |
| ∟∟∟ name                         | string  | 姓名                                                         |
| ∟∟∟ nric                         | string  | 身份证                                                       |
| ∟∟∟ nationality                  | string  | 国家代码<br />[详见附录国家代码](#国家代码)                  |
| ∟∟∟ mobileArea                   | string  | 移动电话区域码                                               |
| ∟∟∟ mobileNumber                 | string  | 移动号码                                                     |
| ∟∟∟ email                        | string  | 电子邮件地址                                                 |
| ∟∟∟ occupation                   | string  | 职业<br />TODO                                               |
| ∟∟∟industryExpYear               | integer | 行业经验年限                                                 |
| ∟∟∟manageExpYear                 | integer | 管理经验年限                                                 |
| ∟∟∟ residenceType                | string  | 住宅类型<br />TODO                                           |
| ∟∟∟ residenceOwnership           | string  | 住宅所有权<br />[参见附录字典代码—所有权类型](#字典代码)     |
| ∟financial                       | object  | 公司财务信息                                                 |
| ∟∟ lastestYearRevenus            | string  | 可选：1-小于1千万；2-1千万到1亿；3-大于1亿                   |
| ∟∟ mainAccountWithOurBank        | string  | 公司主账户是否在我行<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟ outLoanNotWithOutBank         | string  | 是否有不在我行的外部贷款<br />[参见附录字典代码—是/否](#字典代码) |
| ∟uploadDocument                  | array   | 上传文件                                                     |
| ∟∟ documentTemplateId            | integer | 文档模板Id                                                   |
| ∟∟ file                          | string  | 文件                                                         |
| ∟kyc                             | object  | kyc相关信息                                                  |
| ∟∟businessInBlackListArea        | string  | 是否在黑名单地区有业务<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟businessPlanInBlackListArea    | string  | 是否在黑名单地区有开展业务规划<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟businessOrPartnerSanctioned    | string  | 业务或合作伙伴是否被制裁<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟relationInBlackListArea        | string  | 业务关联方是否在黑名单地区<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟repaymentSourceInBlackListArea | string  | 贷款还款来源关联方是否在黑名单地区<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟representsNeutrality           | string  | 业务是否与第三方无关<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟representsNeutralityShared     | string  | 业务是否与第三方共享相关属性，地址、电话号码、受益所有人、授权签字人、员工<br />[参见附录字典代码—是/否](#字典代码) |
| ∟∟familiarWithBusiness           | string  | 业务范围类型的企业，选项：1-化学物质批发商；2-国防相关业务；3-大使馆、领事馆和高级专员公署客户；4-能源和金属采掘业；5-从事批发银行券业务的金融机构；6-持牌赌场、博彩企业和持牌赌场中介；7-持牌放债人；8-货币兑换商和汇款代理商；9-石油和天然气(包括加油)；10-支付服务提供商；11-商品的实物运输；12-纯私人银行；13-航运、海运或船舶租赁(包括加油)；14-没有核心经营业务或基础业务交易的特殊目的实体 |
| ∟referenceAccount                | object  | 关联账户                                                     |
| ∟∟account                        | string  | 账户                                                         |
| ∟∟accountBank                    | string  | 开户行                                                       |

#### 响应体示例

```json
{
    "data":{
      "customerOfferProcedure": {
          "customerOfferId": 123456789,
          "customerId": 1231234556,
          "customerOfferProcess": "MICRO_LOAN_PROCESS",
          "employee": 1231245667,
          "customerOfferProcessNextTask": "LOAN_APPLICATION",
          "status": "RECORD"
      },
      "pdpa": [
          {
              "item":"personalInformation",
              "pdpaInformation":[
                  { "label": "name", "name": "Name" },
                  { "label": "aliasName", "name": "Alias Name" },
                  { "label": "sex", "name": "Sex" },
                  { "label": "dateOfBirth", "name": "Date of Birth" }
              ]
          },
          {
              "item":"corporateInformation",
              "pdpaInformation":[
                  { "label": "entityBasicProfile", "name": "Entity Basic Profile" },
                  { "label": "entityPreviousNames", "name": "Entity Previous Names" },
                  { "label": "entityAddresses", "name": "Entity Addresses" }
              ]
          }
      ],
      "product": {
          "productId": "99990220099",
          "identificationCode": "SM0010",
          "name": "极速贷",
          "version": "1",
          "description": "提供给中小企业极速办理贷款的感受",
          "amountConfiguration": {
            "maxValueRange": "10000000",
            "minValueRange": "10000"
        },
      "termConfiguration": {
          "maxValueRange": "ONE_YEAR",
          "minValueRange": "THREE_MONTHS"
        }
      },
      "loan": {
          "amount": "1000000",
          "currency": "SGP",
          "term": "SIX_MONTHS",
          "local": "Y",
          "employ": "1"
      },
      "company": {
          "regestrationNo": "000666303"
      },
      "contact": {
          "contants":"Mr. A"
          "contactNRIC": "123456789",
          "mobileArea": "86",
          "mobileNumber": "1765498274",
          "email": "xxxx@hotmail.cn"
      },
      "detail": {
          "name": "xxxx company",
          "regestrationNo": "000666303",
          "address": "xxxx street",
          "businessType": "1",
          "contactAddress": "xxxx road",
          "businessPremiseType": "OWNED",
          "businessFocused": 40
      },
      "guarantor": {
          "primaryGuarantor": "123445",
          "guarantors": [
              {
                "name": "Coco",
                "nric": "123445",
                "nationality": "USA",
                "mobileArea": "65",
                "mobileNumber": "1238765447",
                "email": "xxxx@hotmail.cn",
                "occupation": "1",
                "industryExpYear": 6,
                "manageExpYear": 3,
                "residenceType": "1",
                "residenceOwnership": "RENTED"
              },
              {
                "name": "Lili",
                "nric": "123445",
                "nationality": "USA",
                "mobileArea": "65",
                "mobileNumber": "1238765447",
                "email": "xxxx@hotmail.cn",
                "occupation": "1",
                "industryExpYear": 6,
                "manageExpYear": 3,
                "residenceType": "1",
                "residenceOwnership": "RENTED"
              }
          ]
      },
      "financial": {
          "lastestYearRevenus": "1",
          "mainAccountWithOurBank": "Y",
          "outLoanNotWithOutBank": "N"
      },
      "uploadDocument": [
          { "documentTemplateId": 123455312, "file": "weofowusfljwoeuf" },
          { "documentTemplateId": 123453577, "file": "weofowusfljwoeuf" }
      ],
      "kyc": {
          "businessInBlackListArea": "N",
          "businessPlanInBlackListArea": "N",
          "businessOrPartnerSanctioned": "N",
          "relationInBlackListArea": "N",
          "repaymentSourceInBlackListArea": "N",
          "representsNeutrality": "N",
          "representsNeutralityShared": "N",
          "familiarWithBusiness": "14"
      },
      "referenceAccount":{
          "account":"123123",
          "accountBank":"ABC Bank"
      }
    }
}
```

## 查询客户贷款申请

### 请求

| Path        | /customerOffer/loan/{customerId}/list |
| ----------- | ------------------------------------- |
| Method      | GET                                   |
| Description | 查询客户贷款申请                      |

#### 请求头

[见通用请求头](#通用请求头)

#### 路径参数

| 参数       | 类型    | M/O  | 说明   |
| ---------- | ------- | ---- | ------ |
| customerId | integer | M    | 客户Id |

### 响应

#### 响应体

| 参数              | 类型    | 说明                                                 |
| ----------------- | ------- | ---------------------------------------------------- |
| customerOffers    | array   | 客户贷款申请列表                                     |
| ∟ customerOfferId | integer | 申请Id                                               |
| ∟ amount          | string  | 申请金额                                             |
| ∟ datetime        | string  | 申请日期                                             |
| ∟ productName     | string  | 产品名称                                             |
| ∟ status          | string  | 申请状态<br />[参见附录字典代码—申请状态](#字典代码) |

#### 响应体示例

```json
{
  "customerOffers": [
    {
      "customerOfferId": 123456789,
      "amount": 6000000,
      "datetime": "2010-06-07T15:20:00.325Z",
      "productName": "Micro Loan",
      "status": "RECORD"
    },
    {
      "customerOfferId": 54323455,
      "amount": "3000000",
      "datetime": "2010-06-07T15:20:00.325Z",
      "productName": "Micro Loan",
      "status": "APPROVALED"
    }
  ]
}
```

## 查询当期账单

### 请求

| Path        | /invoice/retrieve/{customerId}/current |
| ----------- | -------------------------------------- |
| Method      | GET                                    |
| Description | 查询当期账单                           |

#### 路径参数

| 参数       | 类型    | M/O  | 说明     |
| ---------- | ------- | ---- | -------- |
| customerId | integer | M    | 用户编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                   | 类型   | 说明                                                        |
| ---------------------- | ------ | ----------------------------------------------------------- |
| data                   | array  |                                                             |
| ∟invoicee              | string | 账单                                                        |
| ∟invoiceId             | string | 账单编号                                                    |
| ∟invoiceDueDate        | string | 截止日期                                                    |
| ∟invoicePeriodFromDate | string | 周期起始日期                                                |
| ∟invoicePeriodToDate   | string | 周期截止日期                                                |
| ∟invoiceTotalAmount    | string | 总金额                                                      |
| ∟invoiceCurrency       | string | 币种<br/>[详见附件币种代码](#币种代码)                      |
| ∟invoiceStatus         | string | 账单状态<br/>[参见附录字典代码—账单状态](#字典代码)         |
| ∟repaymentStatus       | string | 还款状态<br/>[参见附录字典代码—还款状态](#字典代码)         |
| ∟agreementId           | string | 协议编号                                                    |
| ∟loanAgreementFromDate | string | 贷款协议日期                                                |
| ∟invoiceRepaymentDate  | string | 账单付款日期                                                |
| ∟invoiceLines          | array  | 账单列表                                                    |
| ∟∟invoiceAmountType    | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](#字典代码) |
| ∟∟invoiceAmount        | string | 账单金额                                                    |

#### 响应体示例

```json
{
    "data":[
        {
            "invoicee":"123",
            "invoiceId":"123",
            "invoiceDueDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
            "invoiceTotalAmount":"123",
            "invoiceCurrency":"CHN",
            "invoiceStatus":"ACCOUNTED",
            "repaymentStatus":"UNDO",
            "agreementId":"123",
            "loanAgreementFromDate":"2010-06-07T15:20:00.325Z",
            "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
            "invoiceLines":[
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                },
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                }
            ]
        },
        {   
            "invoicee":"123",
            "invoiceId":"123",
            "invoiceDueDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
            "invoiceTotalAmount":"123",
            "invoiceCurrency":"CHN",
            "invoiceStatus":"ACCOUNTED",
            "repaymentStatus":"UNDO",
            "agreementId":"123",
            "loanAgreementFromDate":"2010-06-07T15:20:00.325Z",
            "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
            "invoiceLines":[
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                },
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                }
            ]
        }
    ],
    "code":0
}
```

## 提前还款试算

### 请求

| Path        | /ConsumerLoan/prepayment/{agreementId}/calculate |
| ----------- | ------------------------------------------------ |
| Method      | GET                                              |
| Description | 提前还款试算                                     |

#### 路径参数

| 参数        | 类型   | M/O  | 说明     |
| ----------- | ------ | ---- | -------- |
| agreementId | string | M    | 申请编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                | 类型   | 说明                                                        |
| ------------------- | ------ | ----------------------------------------------------------- |
| data                | object |                                                             |
| ∟agreementId        | string | 协议编号                                                    |
| ∟totalAmount        | string | 总额                                                        |
| ∟prepaymentLines    | array  | 账单列表                                                    |
| ∟∟invoiceAmountType | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](#字典代码) |
| ∟∟invoiceAmount     | string | 账单金额                                                    |

#### 响应体示例

```json
{
    "data":{
        "agreementId":"123",
        "totalAmount":"5000",
        "prepaymentLines":[
            {
                "invoiceAmountType":"PRINCIPAL",
                "invoiceAmount":"4000"
            },
            {
                "invoiceAmountType":"FEE",
                "invoiceAmount":"4000"
            }
        ]
    },
    "code":0
}
```

## 账单提前还款

### 请求

| Path        | /ConsumerLoan/repay |
| ----------- | ------------------- |
| Method      | POST                |
| Description | 账单提前还款        |

#### 请求头

[见通用请求头](#通用请求头)

#### 请求体

| 参数               | 类型   | M/O  | 说明                                    |
| ------------------ | ------ | ---- | --------------------------------------- |
| agreementId        | string | M    | 协议编号                                |
| currency           | string | M    | 币种<br />[详见附件币种代码](#币种代码) |
| principal          | string | M    | 本金                                    |
| interest           | string | M    | 利息                                    |
| fee                | string | M    | 费用                                    |
| repaymentAccountId | string | M    | 还款账户编号                            |
| repaymentAccount   | string | M    | 还款账户                                |

#### 请求体示例

```json
{
    "agreementId":"123",
    "currency":"CHN",
    "principal":"5000",
    "interest":"200",
    "fee":"500",
    "repaymentAccountId":"123",
    "repaymentAccount":"123"
}
```

## 账单试算

### 请求

| Path        | /invoice/prepayment/{agreementId}/calculate |
| ----------- | ------------------------------------------- |
| Method      | GET                                         |
| Description | 账单试算                                    |

#### 路径参数

| 参数        | 类型    | M/O  | 说明     |
| ----------- | ------- | ---- | -------- |
| agreementId | integer | M    | 协议编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                | 类型   | 说明                                                        |
| ------------------- | ------ | ----------------------------------------------------------- |
| data                | object |                                                             |
| ∟agreementId        | string | 协议编号                                                    |
| ∟totalAmount        | string | 总额                                                        |
| ∟prepaymentLines    | array  | 账单列表                                                    |
| ∟∟invoiceAmountType | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](#字典代码) |
| ∟∟invoiceAmount     | string | 账单金额                                                    |

#### 响应体示例

```json
{
    "data":{
        "agreementId":"123",
        "totalAmount":"5000",
        "prepaymentLines":[
            {
                "invoiceAmountType":"PRINCIPAL",
                "invoiceAmount":"4000"
            },
            {
                "invoiceAmountType":"FEE",
                "invoiceAmount":"4000"
            }
        ]
    },
    "code":0
}
```

## 账单还款

### 请求

| Path        | /ConsumerLoan/invoice/repay |
| ----------- | --------------------------- |
| Method      | POST                        |
| Description | 账单还款                    |

#### 请求头

[见通用请求头](#通用请求头)

#### 请求体

| 参数               | 类型   | M/O  | 说明                                    |
| ------------------ | ------ | ---- | --------------------------------------- |
| repaymentAccountId | string | M    | 还款账户编号                            |
| repaymentAccount   | string | M    | 还款账户                                |
| amount             | string | M    | 金额                                    |
| invoiceId          | string | M    | 账单编号                                |
| currency           | string | M    | 币种<br />[详见附件币种代码](#币种代码) |

#### 请求体示例

```json
{
    "repaymentAccountId":"123",
    "repaymentAccount":"123",
    "amount":"500000",
    "invoiceId":"123",
    "currency":"CHN"
}
```

### 响应

#### 响应体

| 参数                   | 类型   | 说明                                                         |
| ---------------------- | ------ | ------------------------------------------------------------ |
| data                   | object |                                                              |
| ∟invoicee              | string | 账单                                                         |
| ∟invoiceId             | string | 账单编号                                                     |
| ∟invoiceDueDate        | string | 截止日期                                                     |
| ∟invoicePeriodFromDate | string | 周期起始日期                                                 |
| ∟invoicePeriodToDate   | string | 周期截止日期                                                 |
| ∟invoiceTotalAmount    | string | 总金额                                                       |
| ∟invoiceCurrency       | string | 币种<br/>[详见附件币种代码](#币种代码)                       |
| ∟invoiceStatus         | string | 账单状态<br/>[参见附录字典代码—账单状态](appendices/dictionary_code.md) |
| ∟repaymentStatus       | string | 还款状态<br/>[参见附录字典代码—还款状态](#字典代码)          |
| ∟agreementId           | string | 协议编号                                                     |
| ∟loanAgreementFromDate | string | 贷款协议日期                                                 |
| ∟invoiceRepaymentDate  | string | 账单付款日期                                                 |
| ∟invoiceLines          | array  | 账单列表                                                     |
| ∟∟invoiceAmountType    | string | 账单金额类型<br/>[参见附录字典代码—账单金额类型](#字典代码)  |
| ∟∟invoiceAmount        | string | 账单金额                                                     |

#### 响应体示例

```json
{
    "data":{
            "invoicee":"123",
            "invoiceId":"123",
            "invoiceDueDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodFromDate":"2010-06-07T15:20:00.325Z",
            "invoicePeriodToDate":"2010-06-07T15:20:00.325Z",
            "invoiceTotalAmount":"123",
            "invoiceCurrency":"CHN",
            "invoiceStatus":"ACCOUNTED",
            "repaymentStatus":"UNDO",
            "agreementId":"123",
            "loanAgreementFromDate":"2010-06-07T15:20:00.325Z",
            "invoiceRepaymentDate":"2010-06-07T15:20:00.325Z",
            "invoiceLines":[
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                },
                {
                    "invoiceAmountType":"PRINCIPAL",
                    "invoiceAmount":"123"
                }
            ]
    },
    "code":0
}
```

## 查询还款计划

### 请求

| Path        | /invoice/schedule/{agreementId}/retrieve |
| ----------- | ---------------------------------------- |
| Method      | GET                                      |
| Description | 查询还款计划                             |

#### 路径参数

| 参数        | 类型    | M/O  | 说明     |
| ----------- | ------- | ---- | -------- |
| agreementId | integer | M    | 协议编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                    | 类型    | 说明                                                        |
| ----------------------- | ------- | ----------------------------------------------------------- |
| data                    | object  |                                                             |
| ∟agreementId            | string  | 协议编号                                                    |
| ∟repaymentFrequency     | string  | 还款频率                                                    |
| ∟repaymentDayType       | string  | 还款日类型<br/>[参见附录字典代码—还款日类型](#字典代码)     |
| ∟paymentMethodType      | string  | 还款日期类型<br/>[参见附录字典代码—还款日期类型](#字典代码) |
| ∟fromDate               | string  | 起始日                                                      |
| ∟endDate                | string  | 截止日                                                      |
| ∟totalInstalmentLines   | array   | 总分期信息                                                  |
| ∟∟invoiceAmountType     | string  | 账单金额类型<br/>[参见附录字典代码—账单金额类型](#字典代码) |
| ∟∟invoiceAmount         | string  | 账单金额                                                    |
| ∟scheduleLines          | array   | 计划列表                                                    |
| ∟∟period                | integer | 期数                                                        |
| ∟∟invoiceId             | string  | 账单编号                                                    |
| ∟∟invoiceInstalment     | string  | 账单分期                                                    |
| ∟∟invoicePeriodFromDate | string  | 起始日                                                      |
| ∟∟invoicePeriodToDate   | string  | 截止日                                                      |
| ∟∟invoiceRepaymentDate  | string  | 还款日                                                      |
| ∟∟invoiceLines          | array   | 账单信息                                                    |
| ∟∟∟invoiceAmountType    | string  | 账单金额类型<br/>[参见附录字典代码—账单金额类型](#字典代码) |
| ∟∟∟invoiceAmount        | string  | 账单金额                                                    |

#### 响应体示例

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

## 新建还款账户

### 请求

| Path        | /ConsumerLoan/repaymentAccount |
| ----------- | ------------------------------ |
| Method      | POST                           |
| Description | 新建还款账户                   |

#### 请求头

[见通用请求头](#通用请求头)

#### 请求体

| 参数                 | 类型    | M/O  | 说明         |
| -------------------- | ------- | ---- | ------------ |
| agreementId          | integer | M    | 申请编号     |
| repaymentAccount     | string  | M    | 还款账户     |
| repaymentAccountBank | string  | M    | 还款账户银行 |
| mobileNumber         | string  | M    | 手机号       |

#### 请求体示例

```json
{
    "agreementId":123,
    "repaymentAccount":"123",
    "repaymentAccountBank":"123",
    "mobileNumber":"123"
}
```

### 响应

#### 响应体

| 参数                   | 类型   | 说明                                         |
| ---------------------- | ------ | -------------------------------------------- |
| data                   | object |                                              |
| ∟agreementId           | string | 协议编号                                     |
| ∟repaymentAccountLines | array  | 还款账户列表                                 |
| ∟∟id                   | string |                                              |
| ∟∟repaymentAccount     | string | 还款账户                                     |
| ∟∟repaymentAccountBank | string | 还款账户银行                                 |
| ∟∟status               | string | 状态<br/>[参见附录字典代码—是/否](#字典代码) |

#### 响应体示例

```json
{
    "data":{
        "agreementId":123,
        "repaymentAccountLines":[
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            },
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            }
        ]
    },
    "code":0
}
```

## 查询还款账户列表

### 请求

| Path        | /ConsumerLoan/repaymentAccount/{agreementId}/retrieve |
| ----------- | ----------------------------------------------------- |
| Method      | GET                                                   |
| Description | 查询还款账户列表                                      |

#### 路径参数

| 参数        | 类型   | M/O  | 说明     |
| ----------- | ------ | ---- | -------- |
| agreementId | string | M    | 申请编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                   | 类型   | 说明                                         |
| ---------------------- | ------ | -------------------------------------------- |
| data                   | object |                                              |
| ∟agreementId           | string | 申请编号                                     |
| ∟repaymentAccountLines | array  | 还款账户列表                                 |
| ∟∟id                   | string |                                              |
| ∟∟repaymentAccount     | string | 还款账户                                     |
| ∟∟repaymentAccountBank | string | 还款账户银行                                 |
| ∟∟status               | string | 状态<br/>[参见附录字典代码—是/否](#字典代码) |

#### 响应体示例

```json
{
    "data":{
        "agreementId":123,
        "repaymentAccountLines":[
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            },
            {
                "id":123,
                "repaymentAccount":"123",
                "repaymentAccountBank":"212",
                "status":"Y"
            }
        ]
    },
    "code":0
}
```

## 借款详情

### 请求

| Path        | /ConsumerLoan/LoanAgreement/detail/{agreementId}/retrieve |
| ----------- | --------------------------------------------------------- |
| Method      | GET                                                       |
| Description | 借款详情                                                  |

#### 路径参数

| 参数        | 类型    | M/O  | 说明     |
| ----------- | ------- | ---- | -------- |
| agreementId | integer | M    | 协议编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数                 | 类型   | 说明                                                |
| -------------------- | ------ | --------------------------------------------------- |
| data                 | object |                                                     |
| ∟agreementId         | string | 协议编号                                            |
| ∟productName         | string | 产品名称                                            |
| ∟amount              | string | 账单列表                                            |
| ∟term                | string | 期限<br/>[参见附录字典代码—期限](#字典代码)         |
| ∟disbursementAccount | string | 账单金额                                            |
| ∟purpose             | string | 用途                                                |
| ∟paymentMethod       | string | 还款方式<br/>[参见附录字典代码—还款方式](#字典代码) |
| ∟disbursementBank    | string | 还款银行                                            |
| ∟agreementDocumentId | string | 协议文档编号                                        |

#### 响应体示例

```json
{
    "data":{
        "agreementId":"123",
        "productName":"产品1",
        "amount":"500",
        "term":"ONE_MONTH",
        "disbursementAccount":"9000",
        "purpose":"用途",
        "paymentMethod":"EQUAL_INSTALLMENT",
        "disbursementBank":"ABC Bank",
        "agreementDocumentId":"123"
    },
    "code":0
}
```

## 查询还款记录

### 请求

| Path        | /ConsumerLoan/repayment/record/{customerId}/retrieve |
| ----------- | ---------------------------------------------------- |
| Method      | GET                                                  |
| Description | 查询还款记录                                         |

#### 路径参数

| 参数       | 类型    | M/O  | 说明     |
| ---------- | ------- | ---- | -------- |
| customerId | integer | M    | 用户编号 |

#### 请求头

[见通用请求头](#通用请求头)

### 响应

#### 响应体

| 参数             | 类型    | 说明                                                         |
| ---------------- | ------- | ------------------------------------------------------------ |
| data             | array   |                                                              |
| ∟id              | string  |                                                              |
| ∟repaymentAmount | decimal | 还款金额                                                     |
| ∟currencyType    | string  | 币种<br/>[详见附件币种代码](#币种代码)                       |
| ∟status          | string  | 指令生命周期状态<br/>[参见附录字典代码—指令生命周期状态](#字典代码) |
| ∟payerAccount    | string  | 付款人账户                                                   |
| ∟agreementId     | string  | 协议编号                                                     |
| ∟userId          | string  | 用户编号                                                     |
| ∟customerId      | string  | 客户编号                                                     |
| ∟referenceId     | string  | 关联编号                                                     |
| ∟startDateTime   | string  | 开始日期                                                     |
| ∟endDateTime     | string  | 结束日期                                                     |
| ∟executeDateTime | string  | 执行日期                                                     |

#### 响应体示例

```json
{
    "data":[
        {
            "id":"123",
            "repaymentAmount":50000,
            "status":"FULFILLED",
            "currencyType":"CHN",
            "payerAccount":"123",
            "agreementId":"123",
            "userId":"123",
            "customerId":"123",
            "referenceId":"123",
            "startDateTime":"2010-06-07T15:20:00.325Z",
            "endDateTime":"2010-06-07T15:20:00.325Z",
            "executeDateTime":"2010-06-07T15:20:00.325Z"
        },
        {
            "id":"123",
            "repaymentAmount":50000,
            "status":"FULFILLED",
            "currencyType":"CHN",
            "payerAccount":"123",
            "agreementId":"123",
            "userId":"123",
            "customerId":"123",
            "referenceId":"123",
            "startDateTime":"2010-06-07T15:20:00.325Z",
            "endDateTime":"2010-06-07T15:20:00.325Z",
            "executeDateTime":"2010-06-07T15:20:00.325Z"
        }
    ],
    "code":0
}
```



# 国家代码

| 代码 | 数字 | 英文用名                                     | 中文                      |
| ---- | ---- | -------------------------------------------- | ------------------------- |
| AND  | 020  | Andorra                                      | 安道尔                    |
| ARE  | 784  | United Arab Emirates                         | 阿联酋                    |
| AFG  | 004  | Afghanistan                                  | 阿富汗                    |
| ATG  | 028  | Antigua & Barbuda                            | 安提瓜和巴布达            |
| AIA  | 660  | Anguilla                                     | 安圭拉                    |
| ALB  | 008  | Albania                                      | 阿尔巴尼亚                |
| ARM  | 051  | Armenia                                      | 亚美尼亚                  |
| AGO  | 024  | Angola                                       | 安哥拉                    |
| ATA  | 010  | Antarctica                                   | 南极洲                    |
| ARG  | 032  | Argentina                                    | 阿根廷                    |
| ASM  | 016  | American Samoa                               | 美属萨摩亚                |
| AUT  | 040  | Austria                                      | 奥地利                    |
| AUS  | 036  | Australia                                    | 澳大利亚                  |
| ABW  | 533  | Aruba                                        | 阿鲁巴                    |
| ALA  | 248  | Aland Island                                 | 奥兰群岛                  |
| AZE  | 031  | Azerbaijan                                   | 阿塞拜疆                  |
| BIH  | 070  | Bosnia & Herzegovina                         | 波黑                      |
| BRB  | 052  | Barbados                                     | 巴巴多斯                  |
| BGD  | 050  | Bangladesh                                   | 孟加拉                    |
| BEL  | 056  | Belgium                                      | 比利时                    |
| BFA  | 854  | Burkina                                      | 布基纳法索                |
| BGR  | 100  | Bulgaria                                     | 保加利亚                  |
| BHR  | 048  | Bahrain                                      | 巴林                      |
| BDI  | 108  | Burundi                                      | 布隆迪                    |
| BEN  | 204  | Benin                                        | 贝宁                      |
| BLM  | 652  | Saint Barthélemy                             | 圣巴泰勒米岛              |
| BMU  | 060  | Bermuda                                      | 百慕大                    |
| BRN  | 096  | Brunei                                       | 文莱                      |
| BOL  | 068  | Bolivia                                      | 玻利维亚                  |
| BES  | 535  | Caribbean Netherlands                        | 荷兰加勒比区              |
| BRA  | 076  | Brazil                                       | 巴西                      |
| BHS  | 044  | The Bahamas                                  | 巴哈马                    |
| BTN  | 064  | Bhutan                                       | 不丹                      |
| BVT  | 074  | Bouvet Island                                | 布韦岛                    |
| BWA  | 072  | Botswana                                     | 博茨瓦纳                  |
| BLR  | 112  | Belarus                                      | 白俄罗斯                  |
| BLZ  | 084  | Belize                                       | 伯利兹                    |
| CAN  | 124  | Canada                                       | 加拿大                    |
| CCK  | 166  | Cocos (Keeling) Islands                      | 科科斯群岛                |
| CAF  | 140  | Central African Republic                     | 中非                      |
| CHE  | 756  | Switzerland                                  | 瑞士                      |
| CHL  | 152  | Chile                                        | 智利                      |
| CMR  | 120  | Cameroon                                     | 喀麦隆                    |
| COL  | 170  | Colombia                                     | 哥伦比亚                  |
| CRI  | 188  | Costa Rica                                   | 哥斯达黎加                |
| CUB  | 192  | Cuba                                         | 古巴                      |
| CPV  | 132  | Cape Verde                                   | 佛得角                    |
| CXR  | 162  | Christmas Island                             | 圣诞岛                    |
| CYP  | 196  | Cyprus                                       | 塞浦路斯                  |
| CZE  | 203  | Czech Republic                               | 捷克                      |
| DEU  | 276  | Germany                                      | 德国                      |
| DJI  | 262  | Djibouti                                     | 吉布提                    |
| DNK  | 208  | Denmark                                      | 丹麦                      |
| DMA  | 212  | Dominica                                     | 多米尼克                  |
| DOM  | 214  | Dominican Republic                           | 多米尼加                  |
| DZA  | 012  | Algeria                                      | 阿尔及利亚                |
| ECU  | 218  | Ecuador                                      | 厄瓜多尔                  |
| EST  | 233  | Estonia                                      | 爱沙尼亚                  |
| EGY  | 818  | Egypt                                        | 埃及                      |
| ESH  | 732  | Western Sahara                               | 西撒哈拉                  |
| ERI  | 232  | Eritrea                                      | 厄立特里亚                |
| ESP  | 724  | Spain                                        | 西班牙                    |
| FIN  | 246  | Finland                                      | 芬兰                      |
| FJI  | 242  | Fiji                                         | 斐济群岛                  |
| FLK  | 238  | Falkland Islands                             | 马尔维纳斯群岛（ 福克兰） |
| FSM  | 583  | Federated States of Micronesia               | 密克罗尼西亚联邦          |
| FRO  | 234  | Faroe Islands                                | 法罗群岛                  |
| FRA  | 250  | France                                       | 法国                      |
| GAB  | 266  | Gabon                                        | 加蓬                      |
| GRD  | 308  | Grenada                                      | 格林纳达                  |
| GEO  | 268  | Georgia                                      | 格鲁吉亚                  |
| GUF  | 254  | French Guiana                                | 法属圭亚那                |
| GHA  | 288  | Ghana                                        | 加纳                      |
| GIB  | 292  | Gibraltar                                    | 直布罗陀                  |
| GRL  | 304  | Greenland                                    | 格陵兰                    |
| GIN  | 324  | Guinea                                       | 几内亚                    |
| GLP  | 312  | Guadeloupe                                   | 瓜德罗普                  |
| GNQ  | 226  | Equatorial Guinea                            | 赤道几内亚                |
| GRC  | 300  | Greece                                       | 希腊                      |
| SGS  | 239  | South Georgia and the South Sandwich Islands | 南乔治亚岛和南桑威奇群岛  |
| GTM  | 320  | Guatemala                                    | 危地马拉                  |
| GUM  | 316  | Guam                                         | 关岛                      |
| GNB  | 624  | Guinea-Bissau                                | 几内亚比绍                |
| GUY  | 328  | Guyana                                       | 圭亚那                    |
| HKG  | 344  | Hong Kong                                    | 香港                      |
| HMD  | 334  | Heard Island and McDonald Islands            | 赫德岛和麦克唐纳群岛      |
| HND  | 340  | Honduras                                     | 洪都拉斯                  |
| HRV  | 191  | Croatia                                      | 克罗地亚                  |
| HTI  | 332  | Haiti                                        | 海地                      |
| HUN  | 348  | Hungary                                      | 匈牙利                    |
| IDN  | 360  | Indonesia                                    | 印尼                      |
| IRL  | 372  | Ireland                                      | 爱尔兰                    |
| ISR  | 376  | Israel                                       | 以色列                    |
| IMN  | 833  | Isle of Man                                  | 马恩岛                    |
| IND  | 356  | India                                        | 印度                      |
| IOT  | 086  | British Indian Ocean Territory               | 英属印度洋领地            |
| IRQ  | 368  | Iraq                                         | 伊拉克                    |
| IRN  | 364  | Iran                                         | 伊朗                      |
| ISL  | 352  | Iceland                                      | 冰岛                      |
| ITA  | 380  | Italy                                        | 意大利                    |
| JEY  | 832  | Jersey                                       | 泽西岛                    |
| JAM  | 388  | Jamaica                                      | 牙买加                    |
| JOR  | 400  | Jordan                                       | 约旦                      |
| JPN  | 392  | Japan                                        | 日本                      |
| KHM  | 116  | Cambodia                                     | 柬埔寨                    |
| KIR  | 296  | Kiribati                                     | 基里巴斯                  |
| COM  | 174  | The Comoros                                  | 科摩罗                    |
| KWT  | 414  | Kuwait                                       | 科威特                    |
| CYM  | 136  | Cayman Islands                               | 开曼群岛                  |
| LBN  | 422  | Lebanon                                      | 黎巴嫩                    |
| LIE  | 438  | Liechtenstein                                | 列支敦士登                |
| LKA  | 144  | Sri Lanka                                    | 斯里兰卡                  |
| LBR  | 430  | Liberia                                      | 利比里亚                  |
| LSO  | 426  | Lesotho                                      | 莱索托                    |
| LTU  | 440  | Lithuania                                    | 立陶宛                    |
| LUX  | 442  | Luxembourg                                   | 卢森堡                    |
| LVA  | 428  | Latvia                                       | 拉脱维亚                  |
| LBY  | 434  | Libya                                        | 利比亚                    |
| MAR  | 504  | Morocco                                      | 摩洛哥                    |
| MCO  | 492  | Monaco                                       | 摩纳哥                    |
| MDA  | 498  | Moldova                                      | 摩尔多瓦                  |
| MNE  | 499  | Montenegro                                   | 黑山                      |
| MAF  | 663  | Saint Martin (France)                        | 法属圣马丁                |
| MDG  | 450  | Madagascar                                   | 马达加斯加                |
| MHL  | 584  | Marshall islands                             | 马绍尔群岛                |
| MKD  | 807  | Republic of Macedonia (FYROM)                | 马其顿                    |
| MLI  | 466  | Mali                                         | 马里                      |
| MMR  | 104  | Myanmar (Burma)                              | 缅甸                      |
| MAC  | 446  | Macao                                        | 澳门                      |
| MTQ  | 474  | Martinique                                   | 马提尼克                  |
| MRT  | 478  | Mauritania                                   | 毛里塔尼亚                |
| MSR  | 500  | Montserrat                                   | 蒙塞拉特岛                |
| MLT  | 470  | Malta                                        | 马耳他                    |
| MDV  | 462  | Maldives                                     | 马尔代夫                  |
| MWI  | 454  | Malawi                                       | 马拉维                    |
| MEX  | 484  | Mexico                                       | 墨西哥                    |
| MYS  | 458  | Malaysia                                     | 马来西亚                  |
| NAM  | 516  | Namibia                                      | 纳米比亚                  |
| NER  | 562  | Niger                                        | 尼日尔                    |
| NFK  | 574  | Norfolk Island                               | 诺福克岛                  |
| NGA  | 566  | Nigeria                                      | 尼日利亚                  |
| NIC  | 558  | Nicaragua                                    | 尼加拉瓜                  |
| NLD  | 528  | Netherlands                                  | 荷兰                      |
| NOR  | 578  | Norway                                       | 挪威                      |
| NPL  | 524  | Nepal                                        | 尼泊尔                    |
| NRU  | 520  | Nauru                                        | 瑙鲁                      |
| OMN  | 512  | Oman                                         | 阿曼                      |
| PAN  | 591  | Panama                                       | 巴拿马                    |
| PER  | 604  | Peru                                         | 秘鲁                      |
| PYF  | 258  | French polynesia                             | 法属波利尼西亚            |
| PNG  | 598  | Papua New Guinea                             | 巴布亚新几内亚            |
| PHL  | 608  | The Philippines                              | 菲律宾                    |
| PAK  | 586  | Pakistan                                     | 巴基斯坦                  |
| POL  | 616  | Poland                                       | 波兰                      |
| PCN  | 612  | Pitcairn Islands                             | 皮特凯恩群岛              |
| PRI  | 630  | Puerto Rico                                  | 波多黎各                  |
| PSE  | 275  | Palestinian territories                      | 巴勒斯坦                  |
| PLW  | 585  | Palau                                        | 帕劳                      |
| PRY  | 600  | Paraguay                                     | 巴拉圭                    |
| QAT  | 634  | Qatar                                        | 卡塔尔                    |
| REU  | 638  | Réunion                                      | 留尼汪                    |
| ROU  | 642  | Romania                                      | 罗马尼亚                  |
| SRB  | 688  | Serbia                                       | 塞尔维亚                  |
| RUS  | 643  | Russian Federation                           | 俄罗斯                    |
| RWA  | 646  | Rwanda                                       | 卢旺达                    |
| SLB  | 090  | Solomon Islands                              | 所罗门群岛                |
| SYC  | 690  | Seychelles                                   | 塞舌尔                    |
| SDN  | 729  | Sudan                                        | 苏丹                      |
| SWE  | 752  | Sweden                                       | 瑞典                      |
| SGP  | 702  | Singapore                                    | 新加坡                    |
| SVN  | 705  | Slovenia                                     | 斯洛文尼亚                |
| SJM  | 744  | Template:Country data SJM Svalbard           | 斯瓦尔巴群岛和 扬马延岛   |
| SVK  | 703  | Slovakia                                     | 斯洛伐克                  |
| SLE  | 694  | Sierra Leone                                 | 塞拉利昂                  |
| SMR  | 674  | San Marino                                   | 圣马力诺                  |
| SEN  | 686  | Senegal                                      | 塞内加尔                  |
| SOM  | 706  | Somalia                                      | 索马里                    |
| SUR  | 740  | Suriname                                     | 苏里南                    |
| SSD  | 728  | South Sudan                                  | 南苏丹                    |
| STP  | 678  | Sao Tome & Principe                          | 圣多美和普林西比          |
| SLV  | 222  | El Salvador                                  | 萨尔瓦多                  |
| SYR  | 760  | Syria                                        | 叙利亚                    |
| SWZ  | 748  | Swaziland                                    | 斯威士兰                  |
| TCA  | 796  | Turks & Caicos Islands                       | 特克斯和凯科斯群岛        |
| TCD  | 148  | Chad                                         | 乍得                      |
| TGO  | 768  | Togo                                         | 多哥                      |
| THA  | 764  | Thailand                                     | 泰国                      |
| TKL  | 772  | Tokelau                                      | 托克劳                    |
| TLS  | 626  | Timor-Leste (East Timor)                     | 东帝汶                    |
| TUN  | 788  | Tunisia                                      | 突尼斯                    |
| TON  | 776  | Tonga                                        | 汤加                      |
| TUR  | 792  | Turkey                                       | 土耳其                    |
| TUV  | 798  | Tuvalu                                       | 图瓦卢                    |
| TZA  | 834  | Tanzania                                     | 坦桑尼亚                  |
| UKR  | 804  | Ukraine                                      | 乌克兰                    |
| UGA  | 800  | Uganda                                       | 乌干达                    |
| USA  | 840  | United States of America (USA)               | 美国                      |
| URY  | 858  | Uruguay                                      | 乌拉圭                    |
| VAT  | 336  | Vatican City (The Holy See)                  | 梵蒂冈                    |
| VEN  | 862  | Venezuela                                    | 委内瑞拉                  |
| VGB  | 092  | British Virgin Islands                       | 英属维尔京群岛            |
| VIR  | 850  | United States Virgin Islands                 | 美属维尔京群岛            |
| VNM  | 704  | Vietnam                                      | 越南                      |
| WLF  | 876  | Wallis and Futuna                            | 瓦利斯和富图纳            |
| WSM  | 882  | Samoa                                        | 萨摩亚                    |
| YEM  | 887  | Yemen                                        | 也门                      |
| MYT  | 175  | Mayotte                                      | 马约特                    |
| ZAF  | 710  | South Africa                                 | 南非                      |
| ZMB  | 894  | Zambia                                       | 赞比亚                    |
| ZWE  | 716  | Zimbabwe                                     | 津巴布韦                  |
| CHN  | 156  | China                                        | 中国 内地                 |
| COG  | 178  | Republic of the Congo                        | 刚果（布）                |
| COD  | 180  | Democratic Republic of the Congo             | 刚果（金）                |
| MOZ  | 508  | Mozambique                                   | 莫桑比克                  |
| GGY  | 831  | Guernsey                                     | 根西岛                    |
| GMB  | 270  | Gambia                                       | 冈比亚                    |
| MNP  | 580  | Northern Mariana Islands                     | 北马里亚纳群岛            |
| ETH  | 231  | Ethiopia                                     | 埃塞俄比亚                |
| NCL  | 540  | New Caledonia                                | 新喀里多尼亚              |
| VUT  | 548  | Vanuatu                                      | 瓦努阿图                  |
| ATF  | 260  | French Southern Territories                  | 法属南部领地              |
| NIU  | 570  | Niue                                         | 纽埃                      |
| UMI  | 581  | United States Minor Outlying Islands         | 美国本土外小岛屿          |
| COK  | 184  | Cook Islands                                 | 库克群岛                  |
| GBR  | 826  | Great Britain (United Kingdom; England)      | 英国                      |
| TTO  | 780  | Trinidad & Tobago                            | 特立尼达和多巴哥          |
| VCT  | 670  | St. Vincent & the Grenadines                 | 圣文森特和格林纳丁斯      |
| TWN  | 158  | Taiwan                                       | 中华民国（台湾）          |
| NZL  | 554  | New Zealand                                  | 新西兰                    |
| SAU  | 682  | Saudi Arabia                                 | 沙特阿拉伯                |
| LAO  | 418  | Laos                                         | 老挝                      |
| PRK  | 408  | North Korea                                  | 朝鲜 北朝鲜               |
| KOR  | 410  | South Korea                                  | 韩国 南朝鲜               |
| PRT  | 620  | Portugal                                     | 葡萄牙                    |
| KGZ  | 417  | Kyrgyzstan                                   | 吉尔吉斯斯坦              |
| KAZ  | 398  | Kazakhstan                                   | 哈萨克斯坦                |
| TJK  | 762  | Tajikistan                                   | 塔吉克斯坦                |
| TKM  | 795  | Turkmenistan                                 | 土库曼斯坦                |
| UZB  | 860  | Uzbekistan                                   | 乌兹别克斯坦              |
| KNA  | 659  | St. Kitts & Nevis                            | 圣基茨和尼维斯            |
| SPM  | 666  | Saint-Pierre and Miquelon                    | 圣皮埃尔和密克隆          |
| SHN  | 654  | St. Helena & Dependencies                    | 圣赫勒拿                  |
| LCA  | 662  | St. Lucia                                    | 圣卢西亚                  |
| MUS  | 480  | Mauritius                                    | 毛里求斯                  |
| CIV  | 384  | Côte d'Ivoire                                | 科特迪瓦                  |
| KEN  | 404  | Kenya                                        | 肯尼亚                    |
| MNG  | 496  | Mongolia                                     | 蒙古国 蒙古               |

# 币种代码

| 代码 | 数字 | 英文名称                    | 中文               |
| ---- | ---- | --------------------------- | ------------------ |
| CNY  | 156  | Chinese Yuan                | 人民币             |
| USD  | 840  | United States Dollar        | 美元               |
| EUR  | 978  | Euro                        | 欧元               |
| GBP  | 826  | British Pound               | 英镑               |
| AUD  | 036  | Australia Dollar            | 澳元               |
| CAD  | 124  | Canadian Dollar             | 加元               |
| JPY  | 392  | Japanese Yen                | 日元               |
| HKD  | 344  | Hong Kong Dollar            | 港币               |
| INR  | 356  | Indian Rupee                | 印度               |
| ZAR  | 710  | South African Rand          | 南非兰特           |
| TWD  | 901  | New Taiwan Dollar           | 新台币             |
| MOP  | 446  | Macau Pataca                | 澳门元             |
| KRW  | 410  | South Korean Won            | 韩元               |
| THB  | 764  | Thai Baht                   | 泰铢               |
| NZD  | 554  | New Zealand Dollar          | 新西兰元           |
| SGD  | 702  | Singapore Dollar            | 新加坡元           |
| AED  | 784  | United Arab Emirates Dirham | 阿联酋迪拉姆       |
| AFN  | 971  | Afghan Afghani              | 阿富汗尼           |
| ALL  | 008  | Albania Lek                 | 阿尔巴尼列克       |
| AMD  | 051  | Armenia Dram                | 亚美尼亚德拉姆     |
| ANG  | 532  | Dutch Guilder               | 荷兰盾             |
| AOA  | 973  | Angola Kwanza               | 安哥拉宽扎         |
| ARS  | 032  | Argentina Peso              | 阿根廷比索         |
| AWG  | 533  | Aruba Florin                | 阿鲁巴弗罗林       |
| AZN  | 944  | Azerbaijan Manat            | 阿塞拜疆马纳特     |
| BAM  | 977  | Bosnia Convertible Mark     | 波黑可兑换马克     |
| BBD  | 052  | Barbados Dollar             | 巴巴多斯元         |
| BDT  | 050  | Bangladesh Taka             | 孟加拉国塔卡       |
| BGN  | 975  | Bulgaria Lev                | 保加利亚列弗       |
| BHD  | 048  | Bahrain Dinar               | 巴林第纳尔         |
| BIF  | 108  | Burundi Franc               | 布隆迪法郎         |
| BMD  | 060  | Bermudian Dollar            | 百慕大元           |
| BND  | 096  | Brunei Dollar               | 文莱元             |
| BOB  | 068  | Bolivian Boliviano          | 玻利维亚诺         |
| BRL  | 986  | Brazilian Real              | 巴西雷亚尔         |
| BSD  | 044  | Bahamian Dollar             | 巴哈马元           |
| BTC  |      | BitCoin                     | 比特币             |
| BTN  | 064  | Bhutanese Ngultrum          | 不丹努扎姆         |
| BWP  | 072  | Botswana Pula               | 博茨瓦纳普拉       |
| BYR  | 974  | Belarusian Ruble            | 白俄罗斯卢布       |
| BZD  | 084  | Belize Dollar               | 伯利兹元           |
| CDF  | 976  | Congolese Franc             | 刚果法郎           |
| CHF  | 756  | Swiss Franc                 | 瑞士法郎           |
| CLF  | 990  | Chilean Unidad de Fomento   | 智利比索(基金)     |
| CLP  | 152  | Chilean Peso                | 智利比索           |
| CNH  |      | Chinese Offshore Renminbi   | 中国离岸人民币     |
| COP  | 170  | Colombia Peso               | 哥伦比亚比索       |
| CRC  | 188  | Costa Rica Colon            | 哥斯达黎加科朗     |
| CUP  | 192  | Cuban Peso                  | 古巴比索           |
| CVE  | 132  | Cape Verde Escudo           | 佛得角埃斯库多     |
| CYP  | 196  | Cyprus Pound                | 塞普路斯镑         |
| CZK  | 203  | Czech Republic Koruna       | 捷克克朗           |
| DEM  | 280  | Deutsche Mark               | 德国马克           |
| DJF  | 262  | Djiboutian Franc            | 吉布提法郎         |
| DKK  | 208  | Danish Krone                | 丹麦克朗           |
| DOP  | 214  | Dominican Peso              | 多米尼加比索       |
| DZD  | 012  | Algerian Dinar              | 阿尔及利亚第纳尔   |
| ECS  | 218  | Ecuadorian Sucre            | 厄瓜多尔苏克雷     |
| EGP  | 818  | Egyptian Pound              | 埃及镑             |
| ERN  | 232  | Eritrean Nakfa              | 厄立特里亚纳克法   |
| ETB  | 230  | Ethiopian Birr              | 埃塞俄比亚比尔     |
| FJD  | 242  | Fiji Dollar                 | 斐济元             |
| FKP  | 238  | Falkland Islands Pound      | 福克兰群岛镑       |
| FRF  | 250  | French Franc                | 法国法郎           |
| GEL  | 981  | Georgian Lari               | 格鲁吉亚拉里       |
| GHS  | 936  | Ghanaian Cedi               | 加纳塞地           |
| GIP  | 292  | Gibraltar Pound             | 直布罗陀镑         |
| GMD  | 270  | Gambian Dalasi              | 冈比亚达拉西       |
| GNF  | 324  | Guinean Franc               | 几内亚法郎         |
| GTQ  | 320  | Guatemalan Quetzal          | 危地马拉格查尔     |
| GYD  | 328  | Guyanese Dollar             | 圭亚那元           |
| HNL  | 340  | Honduran Lempira            | 洪都拉斯伦皮拉     |
| HRK  | 191  | Croatian Kuna               | 克罗地亚库纳       |
| HTG  | 332  | Haitian Gourde              | 海地古德           |
| HUF  | 348  | Hungarian Forint            | 匈牙利福林         |
| IDR  | 360  | Indonesian Rupiah           | 印度尼西亚卢比     |
| IEP  | 372  | Irish Pound                 | 爱尔兰镑           |
| ILS  | 376  | Israeli New Shekel          | 以色列新谢克尔     |
| IQD  | 368  | Iraqi Dinar                 | 伊拉克第纳尔       |
| IRR  | 364  | Iranian Rial                | 伊朗里亚尔         |
| ISK  | 352  | Icelandic Krona             | 冰岛克郎           |
| ITL  | 380  | Italian Lira                | 意大利里拉         |
| JMD  | 388  | Jamaican Dollar             | 牙买加元           |
| JOD  | 400  | Jordanian Dinar             | 约旦第纳尔         |
| KES  | 404  | Kenyan Shilling             | 肯尼亚先令         |
| KGS  | 417  | Kyrgyzstani Som             | 吉尔吉斯斯坦索姆   |
| KHR  | 116  | Cambodian Riel              | 柬埔寨瑞尔         |
| KMF  | 174  | Comorian franc              | 科摩罗法郎         |
| KPW  | 408  | North Korean Won            | 朝鲜元             |
| KWD  | 414  | Kuwaiti Dinar               | 科威特第纳尔       |
| KYD  | 136  | Cayman Islands Dollar       | 开曼群岛元         |
| KZT  | 398  | Kazakstani Tenge            | 哈萨克斯坦坚戈     |
| LAK  | 418  | Lao kip                     | 老挝基普           |
| LBP  | 422  | Lebanese Pound              | 黎巴嫩镑           |
| LKR  | 144  | Sri Lankan Rupee            | 斯里兰卡卢比       |
| LRD  | 430  | Liberian dollar             | 利比里亚元         |
| LSL  | 426  | Lesotho Loti                | 莱索托洛蒂         |
| LTL  | 440  | Lithuanian Litas            | 立陶宛立特         |
| LVL  | 428  | Latvian Lats                | 拉脱维亚拉特       |
| LYD  | 434  | Libyan Dinar                | 利比亚第纳尔       |
| MAD  | 504  | Moroccan Dirham             | 摩洛哥迪拉姆       |
| MDL  | 498  | Moldovan Leu                | 摩尔多瓦列伊       |
| MGA  | 969  | Malagasy Ariary             | 马达加斯加阿里亚里 |
| MKD  | 807  | Macedonian Denar            | 马其顿代纳尔       |
| MMK  | 104  | Myanmar Kyat                | 缅甸元             |
| MNT  | 496  | Mongolian Tugrik            | 蒙古图格里克       |
| MRO  | 478  | Mauritania Ouguiya          | 毛里塔尼亚乌吉亚   |
| MUR  | 480  | Mauritian Rupee             | 毛里求斯卢比       |
| MVR  | 462  | Maldives Rufiyaa            | 马尔代夫拉菲亚     |
| MWK  | 454  | Malawian Kwacha             | 马拉维克瓦查       |
| MXN  | 484  | Mexican Peso                | 墨西哥比索         |
| MXV  | 979  | Mexican Unidad De Inversion | 墨西哥(资金)       |
| MYR  | 458  | Malaysian Ringgit           | 林吉特             |
| MZN  | 943  | New Mozambican Metical      | 莫桑比克新梅蒂卡尔 |
| NAD  | 516  | Namibian Dollar             | 纳米比亚元         |
| NGN  | 566  | Nigerian Naira              | 尼日利亚奈拉       |
| NIO  | 558  | Nicaraguan Cordoba Oro      | 尼加拉瓜新科多巴   |
| NOK  | 578  | Norwegian Krone             | 挪威克朗           |
| NPR  | 524  | Nepalese Rupee              | 尼泊尔卢比         |
| OMR  | 512  | Omani Rial                  | 阿曼里亚尔         |
| PAB  | 590  | Panamanian Balboa           | 巴拿马巴波亚       |
| PEN  | 604  | Peruvian Nuevo Sol          | 秘鲁新索尔         |
| PGK  | 598  | Papua New Guinea Kina       | 巴布亚新几内亚基那 |
| PHP  | 608  | Philippine Peso             | 菲律宾比索         |
| PKR  | 586  | Pakistan Rupee              | 巴基斯坦卢比       |
| PLN  | 985  | Polish Zloty                | 波兰兹罗提         |
| PYG  | 600  | Paraguayan Guarani          | 巴拉圭瓜拉尼       |
| QAR  | 634  | Qatari Riyal                | 卡塔尔里亚尔       |
| RON  | 946  | Romanian Leu                | 罗马尼亚列伊       |
| RSD  | 941  | Serbian Dinar               | 塞尔维亚第纳尔     |
| RUB  | 643  | Russian Ruble               | 俄罗斯卢布         |
| RWF  | 646  | Rwandan Franc               | 卢旺达法郎         |
| SAR  | 682  | Saudi Arabian Riyal         | 沙特里亚尔         |
| SBD  | 090  | Solomon Islands Dollar      | 所罗门群岛元       |
| SCR  | 690  | Seychelles Rupee            | 塞舌尔卢比         |
| SDG  | 938  | Sudanese Pound              | 苏丹镑             |
| SEK  | 752  | Swedish Krona               | 瑞典克朗           |
| SHP  | 654  | Saint Helena Pound          | 圣赫勒拿镑         |
| SIT  | 705  | Slovenian Tolar             | 斯洛文尼亚托拉尔   |
| SLL  | 694  | Sierra Leonean Leone        | 塞拉利昂利昂       |
| SOS  | 706  | Somali Shilling             | 索马里先令         |
| SRD  | 968  | Suriname Dollar             | 苏里南元           |
| STD  | 678  | Sao Tome Dobra              | 圣多美多布拉       |
| SVC  | 222  | Salvadoran Colon            | 萨尔瓦多科朗       |
| SYP  | 760  | Syrian Pound                | 叙利亚镑           |
| SZL  | 748  | Swazi Lilangeni             | 斯威士兰里兰吉尼   |
| TJS  | 972  | Tajikistan Somoni           | 塔吉克斯坦索莫尼   |
| TMT  | 934  | Turkmenistan Manat          | 土库曼斯坦马纳特   |
| TND  | 788  | Tunisian Dinar              | 突尼斯第纳尔       |
| TOP  | 776  | Tongan Pa'Anga              | 汤加潘加           |
| TRY  | 949  | Turkish Lira                | 土耳其里拉         |
| TTD  | 780  | Trinidad and Tobago Dollar  | 特立尼达多巴哥元   |
| TZS  | 834  | Tanzanian Shilling          | 坦桑尼亚先令       |
| UAH  | 980  | Ukrainian Hryvnia           | 乌克兰格里夫纳     |
| UGX  | 800  | Ugandan Shilling            | 乌干达先令         |
| UYU  | 858  | Uruguayan Peso              | 乌拉圭比索         |
| UZS  | 860  | Uzbekistani Som             | 乌兹别克斯坦苏姆   |
| VEF  | 937  | Venezuelan Bolivar Fuerte   | 委内瑞拉玻利瓦尔   |
| VND  | 704  | Viet Nam Dong               | 越南盾             |
| VUV  | 548  | Vanuatu Vatu                | 瓦努阿图瓦图       |
| WST  | 882  | Samoa Tala                  | 萨摩亚塔拉         |
| YER  | 886  | Yemeni Rial                 | 也门里亚尔         |
| ZMW  | 967  | Zambian Kwacha              | 赞比亚克瓦查       |
| ZWL  | 716  | Zimbabwean Dollar           | 津巴布韦元         |
| XAF  | 950  | Central African CFA Franc   | 中非法郎           |
| XCD  | 951  | East Caribbean Dollar       | 东加勒比元         |
| XDR  | 960  | IMF Special Drawing Rights  | IMF特别提款权      |
| XOF  | 952  | West African CFA            | 西非法郎           |
| XPF  | 953  | French Pacific Franc        | 太平洋法郎         |

# 字典代码

| 代码                                           | 值                                       | 说明             |
| ---------------------------------------------- | ---------------------------------------- | ---------------- |
| 贷款产品类型（LoanProductType）                | CONSUMER_LOAN                            | 消费贷款         |
|                                                | CORPORATE_LOAN                           | 企业贷款         |
|                                                | MERCHANDISING_LOAN                       | 商品贷款         |
|                                                | MORTGAGE_LOAN                            | 抵押贷款         |
|                                                | SYNDICATED_LOAN                          | 联合贷款         |
| 产品状态（BankingProductStatus）               | INITIATED                                | 发起             |
|                                                | SOLD                                     | 在售             |
|                                                | OBSOLETE                                 | 废弃             |
| 期限（LoanTermType）                           | ONE_MONTH                                | 一个月           |
|                                                | THREE_MONTHS                             | 三个月           |
|                                                | SIX_MONTHS                               | 六个月           |
|                                                | ONE_YEAR                                 | 一年             |
|                                                | TWO_YEAR                                 | 两年             |
|                                                | THREE_YEAR                               | 三年             |
| 申请状态（ApplyStatus）                        | RECORD                                   | 录入             |
|                                                | SUBMIT                                   | 提交             |
|                                                | APPROVALED                               | 批准             |
|                                                | LOAN                                     | 放款             |
|                                                | FINISH                                   | 结束             |
|                                                | REJECTED                                 | 拒绝             |
| 是否（YesOrNo）                                | Y                                        | 是               |
|                                                | N                                        | 否               |
| 所有权类型（OwnershipType）                    | OWNED                                    | 完全拥有         |
|                                                | SERVICING_MORTAGE                        | 拥有抵押贷款中   |
|                                                | RENTED                                   | 租赁             |
| 账单状态（InvoiceStatus）                      | INITIATE                                 | 创建             |
|                                                | ACCOUNTED                                | 记账             |
|                                                | FINISHED                                 | 完成             |
|                                                | CANCEL                                   | 取消             |
|                                                | TEMP                                     | 临时             |
| 还款状态（RepaymentStatus）                    | UNDO                                     | 撤销             |
|                                                | OPEN                                     | 进行中           |
|                                                | CLEAR                                    | 完成             |
|                                                | OVERDUE                                  | 逾期             |
| 账单金额类型（InvoiceAmountType）              | PRINCIPAL                                | 本金             |
|                                                | INTEREST                                 | 利息             |
|                                                | FEE                                      | 费用             |
|                                                | PENALTY_INTEREST                         | 罚息             |
|                                                | INSTALMENT                               | 分期             |
| 还款方式（PaymentMethodType）                  | EQUAL_INSTALLMENT                        | 等额本息         |
|                                                | EQUAL_PRINCIPAL                          | 等额本金         |
|                                                | ONE_OFF_REPAYMENT                        | 到期还本还息     |
|                                                | PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY | 按期付息到期还款 |
| 指令生命周期状态（InstructionLifecycleStatus） | FAILED                                   | 失败             |
|                                                | FULFILLED                                | 完成             |
|                                                | IN_PROGRESS                              | 处理中           |
|                                                | ON_HOLD                                  | 保持             |
|                                                | PREPARED                                 | 准备             |
|                                                | REQUEST                                  | 请求             |
| 还款日类型（RepaymentFrequency）               | ONE_MONTH                                | 一个月           |
|                                                | THREE_MONTHS                             | 三个月           |
|                                                | SIX_MONTHS                               | 六个月           |
|                                                | ONE_YEAR                                 | 一年             |
| 还款日期类型（RepaymentDayType）               | BASE_LOAN_DAY                            | 贷款日           |
|                                                | MONTH_FIRST_DAY                          | 月初             |
|                                                | MONTH_LAST_DAY                           | 月末             |

