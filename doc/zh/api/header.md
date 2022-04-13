# 请求头





| 名称                | 类型   | 必填 | 描述                                                                               |
| ----------------- | ------ | ---- |----------------------------------------------------------------------------------|
| X-Request-Id      | string | 是   | 全局跟踪请求标识，交易链起始请求                                                                 |
| Content-Type      | string | 是   | **固定值**："application/json; charset=utf-8"                                        |
| X-Authorization   | string | 是   | user_access_token<br />[了解更多：获取access_token](./access_token/get_access_token.md) |
| X-Authorization-Username | string | 是   | username<br />         |
| X-Authorization-Tenant | string | 是   | tenant_id<br />      |

