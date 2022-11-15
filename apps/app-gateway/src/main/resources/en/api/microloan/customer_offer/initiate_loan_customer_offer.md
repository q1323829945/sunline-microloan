# Record customer loan application information

## Request

| Path        | /microloan/customerOffer/loan/initiate       |
| ----------- | -------------------------------------------- |
| Method      | POST                                         |
| Description | Record customer loan application information |

### Request header

| Parameter    | Type   | M/O  | Description                            |
| ------------ | ------ | ---- | -------------------------------------- |
| Content-Type | string | M    | **Fixed value**："multipart/form-data" |

[others see](../../header.md)

### Request body

| Parameter              | Type    | M/O  | Description                              |
| ---------------------- | ------- | ---- | ---------------------------------------- |
| customerOfferProcedure | object  | M    | Customer application process information |
| ∟ customerId           | integer | M    | Customer id                              |
| ∟ customerOfferProcess | string  | M    | Customer Offer Process                   |
| ∟ employee             | integer | M    | Employees working on tasks               |
| product                | object  | M    | Product information                      |
| ∟ productId            | string  | M    | Product id                               |
| ∟productName           | string  | M    | product name                             |
| pdpa                   | object  | M    | PDPA information                         |
| ∟pdpaTemplateId        | string  | M    | PDPA template id                         |
| ∟signature             | string  | O    | Signature                                |

### Example of request body

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



## Response

### Response body

| Parameter                      | Type    | Description                                                  |
| ------------------------------ | ------- | ------------------------------------------------------------ |
| customerOfferProcedure         | object  | Customer application process information                     |
| ∟ customerId                   | integer | Customer id                                                  |
| ∟ customerOfferProcess         | string  | Customer Offer Process                                       |
| ∟ employee                     | integer | Employees working on tasks                                   |
| ∟ customerOfferId              | integer | Customer offer id                                            |
| ∟ customerOfferProcessNextTask | string  | Next step in customer application processing                 |
| product                        | object  | Product information                                          |
| ∟ productId                    | integer | Product id                                                   |
| ∟ amountConfiguration          | object  | Amount range                                                 |
| ∟∟ maxValueRange               | string  | The max amount                                               |
| ∟∟ minValueRange               | string  | The min amount                                               |
| ∟ termConfiguration            | object  | Term range                                                   |
| ∟∟ maxValueRange               | string  | The max term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |
| ∟∟ minValueRange               | string  | The min term<br/> [See Appendix Dictionary Code - Term](../../appendices/dictionary_code.md) |

### Example of response body

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

### Error code

| HTTP status code | Error code | Error message   | Propose                                                      |
| ---------------- | ---------- | --------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant  | Whether the incoming tenant of the request header is correct |
| 404              | 5000       | Invalid product | Check whether the productId in the request body is correct   |