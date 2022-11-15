# Retrieve customer loan application

## Request

| Path        | /microloan/customerOffer/loan/{customerOfferId}/{countryCode}/retrieve |
| ----------- | ------------------------------------------------------------ |
| Method      | GET                                                          |
| Description | Retrieve customer loan application                           |

### Request header

[see](../../header.md)

### Path parameter

| Parameter       | Type    | M/O  | Description                                                  |
| --------------- | ------- | ---- | ------------------------------------------------------------ |
| customerOfferId | integer | M    | Customer offer id                                            |
| countryCode     | string  | M    | Country code<br />[See Appendix Country Code](../../appendices/country_code.md) |

## Response

### Response body

| Parameter                        | Type    | Description                                                  |
| -------------------------------- | ------- | ------------------------------------------------------------ |
| data                             | object  |                                                              |
| ∟customerOfferProcedure          | object  | Customer application process information                     |
| ∟∟ customerOfferId               | integer | Customer offer id                                            |
| ∟∟ customerId                    | integer | Customer id                                                  |
| ∟∟ customerOfferProcess          | string  | Customer Offer Process                                       |
| ∟∟ employee                      | integer | Employees working on tasks                                   |
| ∟∟ customerOfferProcessNextTask  | string  | Next step in customer application processing                 |
| ∟∟ status                        | string  | apply status<br />[See Appendix Dictionary Code - Apply status](../../appendices/dictionary_code.md) |
| ∟pdpa                            | object  | PDPA information                                             |
| ∟∟id                             | string  |                                                              |
| ∟∟pdpaInformation                | array   | Information elements                                         |
| ∟∟∟ item                         | string  | Major categories of elements                                 |
| ∟∟∟ information                  | array   | List of primary subclasses                                   |
| ∟∟∟∟label                        | string  | Element key                                                  |
| ∟∟∟∟name                         | string  | Element Name                                                 |
| ∟product                         | object  | Product informatioin                                         |
| ∟∟productId                      | integer | Product id                                                   |
| ∟∟identificationCode             | string  | Product code                                                 |
| ∟∟name                           | string  | Product name                                                 |
| ∟∟version                        | string  | Product version                                              |
| ∟∟description                    | string  | Product description                                          |
| ∟∟amountConfiguration            | object  | Amount range                                                 |
| ∟∟∟ maxValueRange                | string  | The max amount                                               |
| ∟∟∟ minValueRange                | string  | The min amount                                               |
| ∟∟termConfiguration              | object  | Term range                                                   |
| ∟∟∟ maxValueRange                | string  | The max term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟∟∟ minValueRange                | string  | The min term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟loan                            | object  | Loan information                                             |
| ∟∟ amount                        | string  | Loan amount                                                  |
| ∟∟ currency                      | string  | Currency code, default functional currency<br/>[See Appendix Currency Code](../../appendices/currency_code.md) |
| ∟∟ term                          | string  | Term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟∟ local                         | string  | Whether the business operation area is local<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟ employ                        | string  | How many employees are there? Options: 1-10~30; 2-30~50； 3-50~100； Above 4-100 |
| ∟company                         | object  | Company information                                          |
| ∟∟ regestrationNo                | string  | Registration No.                                             |
| ∟contact                         | object  | Contact information                                          |
| ∟∟contacts                       | string  | Contact name                                                 |
| ∟∟ contactNRIC                   | string  | Contact NRIC                                                 |
| ∟∟ mobileArea                    | string  | Mobile area                                                  |
| ∟∟ mobileNumber                  | string  | Mobile number                                                |
| ∟∟ email                         | string  | Email                                                        |
| ∟detail                          | object  | Company detail information                                   |
| ∟∟ name                          | string  | Registration name                                            |
| ∟∟ regestrationNo                | string  | Registration No.                                             |
| ∟∟ address                       | string  | Registration Address                                         |
| ∟∟ businessType                  | string  | Business type                                                |
| ∟∟ contactAddress                | string  | Contact address                                              |
| ∟∟ businessPremiseType           | string  | Business premise Type<br />[See Appendix Dictionary Code - Ownership Type](../../appendices/dictionary_code.md) |
| ∟∟ businessFocused               | integer | The attention of business in manufacturing and service industry. The smaller the value, the more attention will be paid to manufacturing, and the larger the value, the more attention will be paid to service industry |
| ∟guarantor                       | object  | Guarantor information                                        |
| ∟∟ primaryGuarantor              | string  | Primary guarantor                                            |
| ∟∟ guarantors                    | array   | Guarantors                                                   |
| ∟∟∟ name                         | string  | Name                                                         |
| ∟∟∟ nric                         | string  | NRIC                                                         |
| ∟∟∟ nationality                  | string  | Nationality<br />[See Appendix Country Code](../../appendices/country_code.md) |
| ∟∟∟ mobileArea                   | string  | Mobile area                                                  |
| ∟∟∟ mobileNumber                 | string  | Mobile number                                                |
| ∟∟∟ email                        | string  | Email                                                        |
| ∟∟∟ occupation                   | string  | Occupation                                                   |
| ∟∟∟industryExpYear               | integer | Years of industry experience                                 |
| ∟∟∟manageExpYear                 | integer | Years of management experience                               |
| ∟∟∟ residenceType                | string  | Residence Type                                               |
| ∟∟∟ residenceOwnership           | string  | Residence ownership<br />[See Appendix Dictionary Code - Ownership Type](../../appendices/dictionary_code.md) |
| ∟financial                       | object  | The company financial information                            |
| ∟∟ lastestYearRevenus            | string  | Optional: 1 - less than 10 million; 20 million to 100 million; 3 to more than 100 million |
| ∟∟ mainAccountWithOurBank        | string  | Whether the company's main account is in our bank<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟ outLoanNotWithOutBank         | string  | Whether there are external loans not in our bank<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟uploadDocument                  | array   | Document upload                                              |
| ∟∟ documentTemplateId            | integer | Document Template id                                         |
| ∟∟ file                          | string  | file                                                         |
| ∟kyc                             | object  | Kyc information                                              |
| ∟∟businessInBlackListArea        | string  | Whether there is business in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟businessPlanInBlackListArea    | string  | Whether there is a business plan in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟businessOrPartnerSanctioned    | string  | Whether the business or partner is sanctioned<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟relationInBlackListArea        | string  | Whether the business related party is in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟repaymentSourceInBlackListArea | string  | Whether the related party of loan repayment source is in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟representsNeutrality           | string  | Whether the business is unrelated to the third party<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟representsNeutralityShared     | string  | Whether the business shares relevant attributes, address, telephone number, beneficial owner, authorized signatory and employee with the third party<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟∟familiarWithBusiness           | string  | Business scope type enterprises, options: 1 - chemical substance wholesaler; 2 - National defense related business; 3 - Clients of embassies, consulates and high commissions; 4 - Energy and metal mining industry; 5 - Financial institutions engaged in wholesale bank notes business; 6 - licensed casinos, gambling enterprises and licensed casino intermediaries; 7 - licensed money lenders; 8 - Money changers and remittance agents; 9 - Oil and natural gas (including refueling); 10 Payment service providers; 11. Physical transportation of goods; 12 Pure private bank; 13 - Shipping, ocean shipping or ship leasing (including refueling); 14 - Special purpose entities without core business or underlying business transactions |
| ∟referenceAccount                | object  | Reference Account                                            |
| ∟∟account                        | string  | Account                                                      |
| ∟∟accountBank                    | string  | Bank                                                         |

### Example of response body

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

### Error code

| HTTP status code | Error code | Error message   | Propose                                                      |
| ---------------- | ---------- | --------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant  | Whether the incoming tenant of the request header is correct |
| 404              | 5000       | Invalid product | Check whether the product in the loan application has been deleted |