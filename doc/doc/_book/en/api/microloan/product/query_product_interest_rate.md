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
