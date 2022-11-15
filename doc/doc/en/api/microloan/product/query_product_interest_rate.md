# Query loan product term parameters

## Request

| Path        | /microloan/product/interestRate/{productId} |
| ----------- | ------------------------------------------- |
| Method      | GET                                         |
| Description | Query loan product term parameters          |

### Path parameter

| Parameter | Type   | M/O  | Description |
| --------- | ------ | ---- | ----------- |
| productId | string | M    | Product id  |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter | Type  | Description |
| --------- | ----- | ----------- |
| data      | array |             |

### Example of response body

```json
{
    "data":[
		"ONE_MONTH","THREE_MONTHS","SIX_MONTHS"
    ],
    "code":0
}
```

### Error code

| HTTP status code | Error code | Error message              | Propose                                                      |
| ---------------- | ---------- | -------------------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant             | Whether the incoming tenant of the request header is correct |
| 500              | 5101       | Invalid interest rate plan | Configure the product's interest rate plan                   |