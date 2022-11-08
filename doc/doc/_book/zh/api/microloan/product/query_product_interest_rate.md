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
