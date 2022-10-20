# 查询客户信息

## 请求

| Path        | /Customer/{userId} |
| ----------- | ------------------ |
| Method      | GET                |
| Description | 查询客户信息       |

### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| client_id | string | M    | 客户编号 |

### 请求头

| 参数                   | 类型   | M/O  | 说明     |
| ---------------------- | ------ | ---- | -------- |
| client_id              | string | M    |          |
| access_key             | string | M    |          |
| X-Authorization-Tenant | string | M    | 租户代码 |

## 响应

### 响应体

| 参数      | 类型   | 说明     |
| --------- | ------ | -------- |
| data      | object |          |
| ∟id       | string | 用户编号 |
| ∟username | string | 用户名   |
| ∟userId   | string | 客户编号 |

### 响应体示例

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
