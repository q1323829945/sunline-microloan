# Query customer loan applications

## Request

| Path        | /microloan/customerOffer/loan/{customerId}/list |
| ----------- | ----------------------------------------------- |
| Method      | GET                                             |
| Description | Query customer loan applications                |

### Request header

[see](../../header.md)

### Path parameter

| Parameter  | Type    | M/O  | Description |
| ---------- | ------- | ---- | ----------- |
| customerId | integer | M    | Customer id |

## Response

### Response body

| Parameter         | Type    | Description                                                  |
| ----------------- | ------- | ------------------------------------------------------------ |
| customerOffers    | array   | Customer offers                                              |
| ∟ customerOfferId | integer | Customer offer id                                            |
| ∟ amount          | string  | Apply Amount                                                 |
| ∟ datetime        | string  | Apply date                                                   |
| ∟ productName     | string  | Product name                                                 |
| ∟ status          | string  | Apply status<br />[See Appendix Dictionary Code - Apply status](../../appendices/dictionary_code.md) |

### Example of response body

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
