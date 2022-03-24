# 异常处理

定义5类异常

```kotlin
open class BadRequestException:BaseException
open class AuthenticationException:BaseException
open class NotFoundException:BaseException
open class BusinessException:BaseException
open class SystemException:BaseException
```

他们都继承同一个基类，该基类的作用定义异常返回的错误内容结构，与错误响应报文结构映射

```kotlin
open class BaseException(
    val statusCode: ManagementExceptionCode,
    val exceptionMessage: String? = null,
    val user: Long? = null,
    val data: Any? = null
) : Exception()
```

## BadRequestException

处理请求从参数错误的异常

## AuthenticationException

处理认证和授权错误异常

## NotFoundException

处理应有数据但未找到数据的异常

## BusinessException

业务逻辑异常

## SystemException

系统异常，常用于技术组件

## statusCode: ManagementExceptionCode

用ManagementExceptionCode枚举统一管理，便于生成错误码表

## 异常使用

为了程序的易读性，在具体代码开中自定义异常类，类具有明确意思的可读性，自定义类必须继承

5类基础异常类，例如：

```kotlin
class FeeConfigException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.FEE_CONFIG_ERROR,
) : BusinessException(exceptionMessage, statusCode)
```

