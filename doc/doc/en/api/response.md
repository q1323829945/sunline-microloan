# Response

## 1.Response status code

The service response uses HTTP status codes, which are used to indicate the status of the operation.

| HTTP status code | description           | resource                                        |
| ---------------- | --------------------- | ----------------------------------------------- |
| 200              | Success               |                                                 |
| 400              | Bad Request           | Due to the requested parameter validation error |
| 401              | Unauthorized          |                                                 |
| 403              | Forbidden             |                                                 |
| 404              | Not Found             |                                                 |
| 429              | Too Many Requests     |                                                 |
| 500              | Internal Server Error | Service internal logic error or exception       |



## 2.Error response body

| Name              | Type   | M/O  | description   |
| ----------------- | ------ | ---- | ------------- |
| status_code       | string | M    | Error code    |
| exception_message | string | M    | Error message |

### Example of response body

```json
{
  "status_code": "string",
  "exception_message": "string"
}
```
