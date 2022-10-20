# 查询上传模版

## 请求

| Path        | /CustomerOffer/loanUploadTemplate/{productId} |
| ----------- | --------------------------------------------- |
| Method      | GET                                           |
| Description | 查询上传模版                                  |

### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| productId | string | M    | 产品编号 |

### 请求头

| 参数                   | 类型   | M/O  | 说明     |
| ---------------------- | ------ | ---- | -------- |
| client_id              | string | M    |          |
| access_key             | string | M    |          |
| X-Authorization-Tenant | string | M    | 租户代码 |

## 响应

### 响应体

| 数    | 类型   | 说明     |
| ----- | ------ | -------- |
| data  | array  |          |
| ∟id   | string | 模版编号 |
| ∟name | string | 模版名称 |

### 响应体示例

```json
{
    "data":[
        {
            "id":"1",
            "name":"证件"
        },
        {
            "id":"2",
            "name":"流水"
        }
    ],
    "code":0
}
```
