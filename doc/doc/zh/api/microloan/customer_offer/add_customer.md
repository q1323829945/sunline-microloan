# 新增客户

## 请求

| Path        | /microloan/Customer |
| ----------- | ------------------- |
| Method      | POST                |
| Description | 新增客户            |

### 请求头

[详见](../../header.md)

## 请求

### 请求体

| 参数     | 类型   | M/O  | 说明     |
| -------- | ------ | ---- | -------- |
| userId   | string | M    | 客户编号 |
| username | string | O    | 用户名   |

### 请求体示例

```json
{
    "username":"张三",
    "userId":"123"
}
```

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
