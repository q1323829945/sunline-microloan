# 服务响应

## 1.响应状态码

采用HTTP状态码，状态码用于指示执行操作的状态。

| HTTP状态码 | 描述                  | 原因                   |
| ---------- | --------------------- | ---------------------- |
| 200        | Success               |                        |
| 400        | Bad Request           | 因请求的参数校验错误   |
| 401        | Unauthorized          |                        |
| 403        | Forbidden             |                        |
| 404        | Not Found             |                        |
| 429        | Too Many Requests     |                        |
| 500        | Internal Server Error | 服务内部逻辑错误或异常 |



## 2.错误响应体

| 名称              | 类型   | 必填 | 描述     |
| ----------------- | ------ | ---- | -------- |
| status_code       | string | 是   | 错误码   |
| exception_message | string | 是   | 错误信息 |

### 响应体示例

```json
{
  "status_code": "string",
  "exception_message": "string"
}
```
