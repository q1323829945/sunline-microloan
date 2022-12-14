# 查询贷款产品期限参数

## 请求

| Path        | /microloan/product/interestRate/{productId} |
| ----------- | ------------------------------------------- |
| Method      | GET                                         |
| Description | 查询贷款产品期限参数                        |

### 路径参数

| 参数      | 类型   | M/O  | 说明     |
| --------- | ------ | ---- | -------- |
| productId | string | M    | 产品编号 |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数 | 类型  | 说明 |
| ---- | ----- | ---- |
| data | array |      |

### 响应体示例

```json
{
    "data":[
		"ONE_MONTH","THREE_MONTHS","SIX_MONTHS"
    ],
    "code":0
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息           | 排查建议               |
| ---------- | ------ | ------------------ | ---------------------- |
| 404        | 1010   | 租户未找到         | 请求头传入租户是否正确 |
| 404        | 5101   | 产品利率计划未配置 | 配置产品的利率计划     |