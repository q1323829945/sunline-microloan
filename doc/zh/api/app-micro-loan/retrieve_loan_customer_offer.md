# 检索客户贷款申请信息

## 请求

| Path        | /customerOffer/loan/{customerOfferId}/{countryCode}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | 检索客户贷款申请信息                                         |

### 请求头

| 参数                   | 类型   | M/O  | 说明     |
| ---------------------- | ------ | ---- | -------- |
| client_id              | string | M    |          |
| access_key             | string | M    |          |
| X-Authorization-Tenant | string | M    | 租户代码 |

### 路径参数

| 参数            | 类型    | M/O  | 说明                                                         |
| --------------- | ------- | ---- | ------------------------------------------------------------ |
| customerOfferId | integer | M    | 客户申请Id                                                   |
| countryCode     | string  | M    | 国家代码<br />[详见附录国家代码](appendices/country_code.md) |

## 响应

### 响应体

| 参数                             | 类型    | 说明                                                         |
| -------------------------------- | ------- | ------------------------------------------------------------ |
| data                             | object  |                                                              |
| ∟customerOfferProcedure          | object  | 客户申请流程信息                                             |
| ∟∟ customerOfferId               | integer | 申请Id                                                       |
| ∟∟ customerId                    | integer | 客户Id                                                       |
| ∟∟ customerOfferProcess          | string  | 客户申请处理流程<br />TODO                                   |
| ∟∟ employee                      | integer | 处理任务的员工                                               |
| ∟∟ customerOfferProcessNextTask  | string  | 下一步任务<br />TODO                                         |
| ∟∟ status                        | string  | 申请状态<br />[详见附录申请状态](appendices/dictionary_code.md) |
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
| ∟∟∟ maxValueRange                | string  | 最大期限<br />[参见附录字典代码—期限](appendices/dictionary_code.md) |
| ∟∟∟ minValueRange                | string  | 最小期限<br />[参见附录字典代码—期限](appendices/dictionary_code.md) |
| ∟loan                            | object  | 贷款申请信息                                                 |
| ∟∟ amount                        | string  | 申请金额                                                     |
| ∟∟ currency                      | string  | 币种代码，默认本币<br />[详见附件币种代码](appendices/currency_code.md) |
| ∟∟ term                          | string  | 期限<br />[参见附录字典代码—期限](appendices/dictionary_code.md) |
| ∟∟ local                         | string  | 业务经营地区是否为本地<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
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
| ∟∟ businessPremiseType           | string  | 经营场所类型<br />[参见附录字典代码—所有权类型](appendices/dictionary_code.md) |
| ∟∟ businessFocused               | integer | 业务在制造业和服务业的关注度，数值越小越关注于制造业，数值越大越关注于服务业 |
| ∟guarantor                       | object  | 担保人信息                                                   |
| ∟∟ primaryGuarantor              | string  | 主担保人的身份证                                             |
| ∟∟ guarantors                    | array   | 担保人列表                                                   |
| ∟∟∟ name                         | string  | 姓名                                                         |
| ∟∟∟ nric                         | string  | 身份证                                                       |
| ∟∟∟ nationality                  | string  | 国家代码<br />[详见附录国家代码](../appendices/country_code.md) |
| ∟∟∟ mobileArea                   | string  | 移动电话区域码                                               |
| ∟∟∟ mobileNumber                 | string  | 移动号码                                                     |
| ∟∟∟ email                        | string  | 电子邮件地址                                                 |
| ∟∟∟ occupation                   | string  | 职业<br />TODO                                               |
| ∟∟∟industryExpYear               | integer | 行业经验年限                                                 |
| ∟∟∟manageExpYear                 | integer | 管理经验年限                                                 |
| ∟∟∟ residenceType                | string  | 住宅类型<br />TODO                                           |
| ∟∟∟ residenceOwnership           | string  | 住宅所有权<br />[参见附录字典代码—所有权类型](appendices/dictionary_code.md) |
| ∟financial                       | object  | 公司财务信息                                                 |
| ∟∟ lastestYearRevenus            | string  | 可选：1-小于1千万；2-1千万到1亿；3-大于1亿                   |
| ∟∟ mainAccountWithOurBank        | string  | 公司主账户是否在我行<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟∟ outLoanNotWithOutBank         | string  | 是否有不在我行的外部贷款<br />[参见附录字典代码—是/否](../appendices/dictionary_code.md) |
| ∟uploadDocument                  | array   | 上传文件                                                     |
| ∟∟ documentTemplateId            | integer | 文档模板Id                                                   |
| ∟∟ file                          | string  | 文件                                                         |
| ∟kyc                             | object  | kyc相关信息                                                  |
| ∟∟businessInBlackListArea        | string  | 是否在黑名单地区有业务<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟businessPlanInBlackListArea    | string  | 是否在黑名单地区有开展业务规划<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟businessOrPartnerSanctioned    | string  | 业务或合作伙伴是否被制裁<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟relationInBlackListArea        | string  | 业务关联方是否在黑名单地区<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟repaymentSourceInBlackListArea | string  | 贷款还款来源关联方是否在黑名单地区<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟representsNeutrality           | string  | 业务是否与第三方无关<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟representsNeutralityShared     | string  | 业务是否与第三方共享相关属性，地址、电话号码、受益所有人、授权签字人、员工<br />[参见附录字典代码—是/否](appendices/dictionary_code.md) |
| ∟∟familiarWithBusiness           | string  | 业务范围类型的企业，选项：1-化学物质批发商；2-国防相关业务；3-大使馆、领事馆和高级专员公署客户；4-能源和金属采掘业；5-从事批发银行券业务的金融机构；6-持牌赌场、博彩企业和持牌赌场中介；7-持牌放债人；8-货币兑换商和汇款代理商；9-石油和天然气(包括加油)；10-支付服务提供商；11-商品的实物运输；12-纯私人银行；13-航运、海运或船舶租赁(包括加油)；14-没有核心经营业务或基础业务交易的特殊目的实体 |
| ∟referenceAccount                | object  | 关联账户                                                     |
| ∟∟account                        | string  | 账户                                                         |
| ∟∟accountBank                    | string  | 开户行                                                       |

### 响应体示例

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
