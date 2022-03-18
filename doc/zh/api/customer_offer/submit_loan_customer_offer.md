# 提交客户贷款申请

## 请求

| Path        | /customerOffer/loan/{customerOfferId}/submit |
| ----------- | -------------------------------------------- |
| Method      | PUT                                          |
| Description | 提交客户贷款申请信息                         |

### 请求头

| 名称         | 类型   | 必填 | 描述                              |
| ------------ | ------ | ---- | --------------------------------- |
| Content-Type | string | 是   | **固定值**："multipart/form-data" |

[其他属性详见](../header.md)

### 路径参数

| 参数            | 类型    | M/O  | 描述       |
| --------------- | ------- | ---- | ---------- |
| customerOfferId | integer | M    | 客户申请Id |

### 请求体

| 参数                            | 类型    | M/O  | 描述                                                         |
| ------------------------------- | ------- | ---- | ------------------------------------------------------------ |
| loan                            | object  | O    | 贷款申请信息                                                 |
| ∟ amount                        | number  | M?   | 申请金额                                                     |
| ∟ currency                      | string  | M?   | 币种代码，默认本币<br />[详见附件币种代码](../appendices/currency_code.md) |
| ∟ term                          | string  | M?   | 期限<br />[参见附录字典代码—期限](../appendices/dictionary_code.md) |
| ∟ local                         | string  | M?   | 业务经营地区是否为本地<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟ employ                        | string  | M?   | 有多少员工，选项：1-10~30；2-30~50；3-50~100；4-100以上      |
| company                         | object  | O    | 公司信息                                                     |
| ∟ regestrationNo                | string  | M?   | 注册号                                                       |
| contact                         | object  | M?   | 联系人信息                                                   |
| ∟ contactNRIC                   | string  | M?   | 联系人身份证                                                 |
| ∟ mobileArea                    | string  | M?   | 移动电话区域码                                               |
| ∟ mobileNumber                  | string  | M?   | 移动号码                                                     |
| ∟ email                         | string  | M?   | 电子邮件地址                                                 |
| detail                          | object  | O    | 公司详细信息                                                 |
| ∟ name                          | string  | M?   | 注册名称                                                     |
| ∟ regestrationNo                | string  | M?   | 注册号                                                       |
| ∟ address                       | string  | M?   | 注册地址                                                     |
| ∟ businessType                  | string  | M?   | 业务类型<br />TODO                                           |
| ∟ contactAddress                | string  | M?   | 联系地址                                                     |
| ∟ businessPremiseType           | string  | M?   | 经营场所类型<br />[参见附录字典代码—所有权类型](../appendices/dictionary_code.md) |
| ∟ businessFocused               | integer | M?   | 业务在制造业和服务业的关注度，数值越小越关注于制造业，数值越大越关注于服务业 |
| guarantor                       | object  | O    | 担保人信息                                                   |
| ∟ primaryGuarantor              | string  | M?   | 主担保人的身份证                                             |
| ∟ guarantors                    | array   | M?   | 担保人列表                                                   |
| ∟∟ name                         | string  | M?   | 姓名                                                         |
| ∟∟ NRIC                         | string  | M?   | 身份证                                                       |
| ∟∟ nationality                  | string  | M?   | 国家代码<br />[详见附录国家代码](../appendices/country_code.md) |
| ∟∟ mobileArea                   | string  | M?   | 移动电话区域码                                               |
| ∟∟ mobileNumber                 | string  | M?   | 移动号码                                                     |
| ∟∟ email                        | string  | M?   | 电子邮件地址                                                 |
| ∟∟ occupation                   | string  | M?   | 职业<br />TODO                                               |
| ∟∟industryExpYear               | integer | M?   | 行业经验年限                                                 |
| ∟∟manageExpYear                 | integer | M?   | 管理经验年限                                                 |
| ∟∟ residenceType                | string  | M?   | 住宅类型<br />[参见附录字典代码—住宅类型](../appendices/dictionary_code.md) |
| ∟∟ residenceOwnership           | string  | M?   | 住宅所有权<br />[参见附录字典代码—所有权类型](../appendices/dictionary_code.md) |
| financial                       | object  | O    | 公司财务信息                                                 |
| ∟ lastestYearRevenus            | string  | M?   | 可选：1-小于1千万；2-1千万到1亿；3-大于1亿                   |
| ∟ mainAccountWithOurBank        | string  | M?   | 公司主账户是否在我行<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟ outLoanNotWithOutBank         | string  | M?   | 是否有不在我行的外部贷款<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| uploadDocument                  | array   | O    | 上传文件                                                     |
| ∟ documentTemplateId            | integer | M?   | 文档模板Id                                                   |
| ∟ file                          | file    | M?   | 文件                                                         |
| kyc                             | object  | O    | kyc相关信息                                                  |
| ∟businessInBlackListArea        | string  | M?   | 是否在黑名单地区有业务<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟businessPlanInBlackListArea    | string  | M?   | 是否在黑名单地区有开展业务规划<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟businessOrPartnerSanctioned    | string  | M?   | 业务或合作伙伴是否被制裁<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟relationInBlackListArea        | string  | M?   | 业务关联方是否在黑名单地区<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟repaymentSourceInBlackListArea | string  | M?   | 贷款还款来源关联方是否在黑名单地区<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟representsNeutrality           | string  | M?   | 业务是否与第三方无关<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟representsNeutralityShared     | string  | M?   | 业务是否与第三方共享相关属性，地址、电话号码、受益所有人、授权签字人、员工<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟familiarWithBusiness           | string  | M?   | 业务范围类型的企业，选项：1-化学物质批发商；2-国防相关业务；3-大使馆、领事馆和高级专员公署客户；4-能源和金属采掘业；5-从事批发银行券业务的金融机构；6-持牌赌场、博彩企业和持牌赌场中介；7-持牌放债人；8-货币兑换商和汇款代理商；9-石油和天然气(包括加油)；10-支付服务提供商；11-商品的实物运输；12-纯私人银行；13-航运、海运或船舶租赁(包括加油)；14-没有核心经营业务或基础业务交易的特殊目的实体 |

### 请求体示例

```json
{
  "loan": {
    "amount": 1000000,
    "currency": "SGP",
    "term": "SIX_MONTHS",
    "local": "Y",
    "employ": "1"
  },
  "company": {
    "regestrationNo": "000666303"
  },
  "contact": {
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
        "NRIC": "123445",
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
        "NRIC": "123445",
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
  }
}
```

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

