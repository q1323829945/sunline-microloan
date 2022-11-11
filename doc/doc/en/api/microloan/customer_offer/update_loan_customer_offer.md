# Update customer loan application

## Request

| Path        | /microloan/customerOffer/loan/{customerOfferId}/update |
| ----------- | ------------------------------------------------------ |
| Method      | PUT                                                    |
| Description | Update customer loan application                       |

### Request header

| Parameter    | Type   | M/O  | Description                            |
| ------------ | ------ | ---- | -------------------------------------- |
| Content-Type | string | M    | **Fixed value**："multipart/form-data" |

[others see](../../header.md)

### Path parameter

| Parameter       | Type    | M/O  | Description       |
| --------------- | ------- | ---- | ----------------- |
| customerOfferId | integer | M    | Customer offer id |

### Request body

| Parameter                       | Type    | M/O  | Description                                                  |
| ------------------------------- | ------- | ---- | ------------------------------------------------------------ |
| loan                            | object  | M    | Loan information                                             |
| ∟ amount                        | string  | M    | Loan amount                                                  |
| ∟ currency                      | string  | M    | Currency code, default functional currency<br/>[See Appendix Currency Code](../../appendices/currency_code.md) |
| ∟ term                          | string  | M    | Term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟ local                         | string  | M    | Whether the business operation area is local<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟ employ                        | string  | M    | How many employees are there? Options: 1-10~30; 2-30~50； 3-50~100； Above 4-100 |
| company                         | object  | M    | Company information                                          |
| ∟ regestrationNo                | string  | M    | Registration No.                                             |
| contact                         | object  | M    | Contact information                                          |
| ∟contacts                       | string  | M    | Contact name                                                 |
| ∟ contactNRIC                   | string  | M    | Contact NRIC                                                 |
| ∟ mobileArea                    | string  | M    | Mobile area                                                  |
| ∟ mobileNumber                  | string  | M    | Mobile number                                                |
| ∟ email                         | string  | M    | Email                                                        |
| detail                          | object  | M    | Company detail information                                   |
| ∟ name                          | string  | M    | Registration name                                            |
| ∟ regestrationNo                | string  | M    | Registration No.                                             |
| ∟ address                       | string  | M    | Registration Address                                         |
| ∟ businessType                  | string  | M    | Business type                                                |
| ∟ contactAddress                | string  | M    | Contact address                                              |
| ∟ businessPremiseType           | string  | M    | Business premise Type<br />[See Appendix Dictionary Code - Ownership Type](../../appendices/dictionary_code.md) |
| ∟ businessFocused               | integer | M    | The attention of business in manufacturing and service industry. The smaller the value, the more attention will be paid to manufacturing, and the larger the value, the more attention will be paid to service industry |
| guarantor                       | object  | M    | Guarantor information                                        |
| ∟ primaryGuarantor              | string  | M    | Primary guarantor                                            |
| ∟ guarantors                    | array   | M    | Guarantors                                                   |
| ∟∟ name                         | string  | M    | Name                                                         |
| ∟∟ nric                         | string  | M    | NRIC                                                         |
| ∟∟ nationality                  | string  | M    | Nationality<br />[See Appendix Country Code](../../appendices/country_code.md) |
| ∟∟ mobileArea                   | string  | M    | Mobile area                                                  |
| ∟∟ mobileNumber                 | string  | M    | Mobile number                                                |
| ∟∟ email                        | string  | M    | Email                                                        |
| ∟∟ occupation                   | string  | M    | Occupation                                                   |
| ∟∟industryExpYear               | integer | M    | Years of industry experience                                 |
| ∟∟manageExpYear                 | integer | M    | Years of management experience                               |
| ∟∟ residenceType                | string  | M    | Residence Type                                               |
| ∟∟ residenceOwnership           | string  | M    | Residence ownership<br />[See Appendix Dictionary Code - Ownership Type](../../appendices/dictionary_code.md) |
| financial                       | object  | M    | The company financial information                            |
| ∟ lastestYearRevenus            | string  | M    | Optional: 1 - less than 10 million; 20 million to 100 million; 3 to more than 100 million |
| ∟ mainAccountWithOurBank        | string  | M    | Whether the company's main account is in our bank<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟ outLoanNotWithOutBank         | string  | M    | Whether there are external loans not in our bank<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| uploadDocument                  | array   | O    | Document upload                                              |
| ∟ documentTemplateId            | integer | M?   | Document Template id                                         |
| ∟ file                          | string  | M?   | file name format ：documentTemplateId/fileName               |
| kyc                             | object  | M    | Kyc information                                              |
| ∟businessInBlackListArea        | string  | M    | Whether there is business in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟businessPlanInBlackListArea    | string  | M    | Whether there is a business plan in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟businessOrPartnerSanctioned    | string  | M    | Whether the business or partner is sanctioned<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟relationInBlackListArea        | string  | M    | Whether the business related party is in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟repaymentSourceInBlackListArea | string  | M    | Whether the related party of loan repayment source is in the blacklist area<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟representsNeutrality           | string  | M    | Whether the business is unrelated to the third party<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟representsNeutralityShared     | string  | M    | Whether the business shares relevant attributes, address, telephone number, beneficial owner, authorized signatory and employee with the third party<br />[See Appendix Dictionary Code - Yes or No](../../appendices/dictionary_code.md) |
| ∟familiarWithBusiness           | string  | M    | Business scope type enterprises, options: 1 - chemical substance wholesaler; 2 - National defense related business; 3 - Clients of embassies, consulates and high commissions; 4 - Energy and metal mining industry; 5 - Financial institutions engaged in wholesale bank notes business; 6 - licensed casinos, gambling enterprises and licensed casino intermediaries; 7 - licensed money lenders; 8 - Money changers and remittance agents; 9 - Oil and natural gas (including refueling); 10 Payment service providers; 11. Physical transportation of goods; 12 Pure private bank; 13 - Shipping, ocean shipping or ship leasing (including refueling); 14 - Special purpose entities without core business or underlying business transactions |
| referenceAccount                | object  | M    | Reference Account                                            |
| ∟account                        | string  | M    | Account                                                      |
| ∟accountBank                    | string  | M    | Bank                                                         |

### Example of request body

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
      },
    "referenceAccount":{
        "account":"99966",
        "accountBank":"abcBank"
    }
}

contentType: application:json
```

```
key: files --Multiple
value: file --File Naming Rules： documentTemplateId/fileName
contentType: multipart/form-data
```



## Response

### Response body

| Parameter | Type | Description |
| --------- | ---- | ----------- |
|           |      |             |

### Example of response body

```json
{
}
```