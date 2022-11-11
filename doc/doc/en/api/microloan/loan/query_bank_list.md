# 查询银行列表

## 请求

| Path        | /ConsumerLoan/bankList/retrieve |
| ----------- | ------------------------------- |
| Method      | GET                             |
| Description | 查询银行列表                    |

### 请求头

[详见](../../header.md)

## 响应

### 响应体

| 参数  | 类型   | 说明     |
| ----- | ------ | -------- |
| data  | array  |          |
| ∟id   | string |          |
| ∟name | string | 银行名称 |
| ∟code | string | 银行代码 |

### 响应体示例

```json
{
    "data":[
		{
            "id":"1",
            "name":"bank1",
            "code":"1"
        },
		{
            "id":"2",
            "name":"bank2",
            "code":"2"
        }
    ],
    "code":0
}
```
