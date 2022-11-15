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

### Error code

| HTTP status code | Error code | Error message           | Propose                                                      |
| ---------------- | ---------- | ----------------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant          | Whether the incoming tenant of the request header is correct |
| 404              | 5201       | Invalid upload tempalte | Configure the upload template of the product                 |

