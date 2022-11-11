# Query loan products

## Request

| Path        | /microloan/product  |
| ----------- | ------------------- |
| Method      | GET                 |
| Description | Query loan products |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter            | Type   | Description                                                  |
| -------------------- | ------ | ------------------------------------------------------------ |
| data                 | array  |                                                              |
| ∟id                  | string | Product id                                                   |
| ∟identificationCode  | string | Unique product identification number                         |
| ∟name                | string | Product name                                                 |
| ∟description         | string | Product Description                                          |
| ∟loanProductType     | string | Product Type<br/> [See Appendix Dictionary Code - Loan Product Type](../../appendices/dictionary_code.md) |
| ∟loanPurpose         | string | purpose                                                      |
| ∟status              | string | Product status<br/> [See Appendix Dictionary Code - Loan Product Status](../../appendices/dictionary_code.md) |
| ∟amountConfiguration | object | Amount range                                                 |
| ∟∟id                 | string |                                                              |
| ∟∟maxValueRange      | string | The max amount                                               |
| ∟∟minValueRange      | string | The min amount                                               |
| ∟termConfiguration   | object | Term range                                                   |
| ∟∟id                 | string |                                                              |
| ∟∟maxValueRange      | string | The max term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟∟minValueRange      | string | The min term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |

### Example of response body

```json
{
    "data":[
        {
            "id":"95123333",
            "identificationCode":"111222333",
            "name":"productA",
            "description":"this is a product",
            "loanProductType":"CONSUMER_LOAN",
            "loanPurpose":"purpose",
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
            "name":"productA",
            "description":"this is a product",
            "loanProductType":"CONSUMER_LOAN",
            "loanPurpose":"purpose",
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
