# Add customer

## Request

| Path        | /microloan/Customer |
| ----------- | ------------------- |
| Method      | POST                |
| Description | Add customer        |

### Request header

[see](../../header.md)

### Request body

| Parameter | Type   | M/O  | Description |
| --------- | ------ | ---- | ----------- |
| userId    | string | M    | user id     |
| username  | string | O    | username    |

### Example of request body

```json
{
    "username":"Mr.Bean",
    "userId":"123"
}
```

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
        "username":"张三",
        "userId":"123"
    },
    "code":0
}
```

### Error code

| HTTP status code | Error code | Error message  | Propose                                                      |
| ---------------- | ---------- | -------------- | ------------------------------------------------------------ |
| 404              | 1010       | Invalid tenant | Whether the incoming tenant of the request header is correct |