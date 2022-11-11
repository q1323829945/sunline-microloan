# Query customer information

## Request

| Path        | /microloan/Customer/{userId} |
| ----------- | ---------------------------- |
| Method      | GET                          |
| Description | Query customer information   |

### Path parameter

| Parameter | Type   | M/O  | Description |
| --------- | ------ | ---- | ----------- |
| userId    | string | M    | user id     |

### Request header

[see](../../header.md)

## Response

### Response body

| Parameter | Type   | Description |
| --------- | ------ | ----------- |
| data      | object |             |
| ∟id       | string | customer id |
| ∟username | string | username    |
| ∟userId   | string | user id     |

### Example of response body

```json
{
    "data":{
        "id":"123",
        "username":"Mr.Bean",
        "userId":"123"
    },
    "code":0
}
```
