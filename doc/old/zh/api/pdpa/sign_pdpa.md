# 签署PDPA协议

## 请求

| Path        | /pdpa/sign       |
| ----------- | ---------------- |
| Method      | POST             |
| Description | 签署PDPA授权协议 |

### 请求头

| 名称         | 类型   | 必填 | 描述                              |
| ------------ | ------ | ---- | --------------------------------- |
| Content-Type | string | 是   | **固定值**："multipart/form-data" |

[其他属性详见](../header.md)

### 请求参数

| 参数           | 类型   | 必须 | 说明           |
| -------------- | ------ | ---- | -------------- |
| pdpaTemplateId | string | 是   | PDPA协议模板Id |
| signature      | png    | 是   | 签名图片       |

## 响应

### 响应体

| 参数 | 类型 | 说明 |
| ---- | ---- | ---- |
|      |      |      |

### 响应体示例

```json
{
}
```

### 错误码

| HTTP状态码 | 错误码 | 错误信息 | 排查建议 |
| ---------- | ------ | -------- | -------- |
|            |        |          |          |
