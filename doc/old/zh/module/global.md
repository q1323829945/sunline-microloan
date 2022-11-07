# 国际化

## 1. 日期时间

接口时间日期采用ISO标准格式yyyy-MM-dd'T'HH:mm:ss.SSSXXX

### 后端服务

- 以租户的时区为准
- 将前端传输的时间转成租户时区时间（可使用TenantDateTime工具类toTenantDateTime）。
- 将DateTime转成Date类型进行数据存储，存储类型为@Temporal(TemporalType.TIMESTAMP)
- 从存储中获取的数据转成租户时区时间（可使用TenantDateTime工具类toTenantDateTime）
- 日期时间计算使用jodaTime工具包

###  前端

根据用户的喜好进行时间处理，包括如下

- 用户喜好的时间展示格式
- 用户所在时区转换



代码示例如下：

````kotlin
//前端转后端
val dt:String = "2022-12-01T12:01:00.123+08:00"

@Autowired val tenantDateTime: TenantDateTime
val dateTime = tenantDateTime.toTenantDateTime(dt)

//表对象日期类型定义
@Temporal(TemporalType.TIMESTAMP)
var fromDate:Date

//后端转表对象
fromDate = dateTime.toDate()

//表对象转后端
val convertDateTime = tenantDateTime.toTenantDateTime(fromDate)

//后端转前端
val dto:String = convertDateTime.toString()
````





