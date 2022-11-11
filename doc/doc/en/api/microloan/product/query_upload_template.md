# Query file upload template

## Request

| Path        | /microloan/CustomerOffer/loanUploadTemplate/{productId} |
| ----------- | ------------------------------------------------------- |
| Method      | GET                                                     |
| Description | Query file upload template                              |

### Path parameter

| Parameter | Type   | M/O  | Description |
| --------- | ------ | ---- | ----------- |
| productId | string | M    | product id  |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter | Type   | Description   |
| --------- | ------ | ------------- |
| data      | array  |               |
| ∟id       | string | Template id   |
| ∟name     | string | Template name |

### Example of response body

```json
{
    "data":[
        {
            "id":"1",
            "name":"certificates"
        },
        {
            "id":"2",
            "name":"turnover"
        }
    ],
    "code":0
}
```
