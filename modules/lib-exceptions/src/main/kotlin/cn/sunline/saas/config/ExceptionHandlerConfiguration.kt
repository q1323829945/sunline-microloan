package cn.sunline.saas.config

import cn.sunline.saas.exceptions.*
import cn.sunline.saas.response.DTOResponseError
import cn.sunline.saas.response.response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandlerConfiguration : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        BadRequestException::class, NotFoundException::class, BusinessException::class, SystemException::class,ManagementException::class
    )
    fun handleCustomerException(ex: BaseException): ResponseEntity<Any> {
        val status = when (ex) {
            is BadRequestException -> HttpStatus.BAD_REQUEST
            is NotFoundException -> HttpStatus.NOT_FOUND
            is BusinessException -> HttpStatus.INTERNAL_SERVER_ERROR
            is SystemException -> HttpStatus.INTERNAL_SERVER_ERROR
            else -> {
                HttpStatus.BAD_REQUEST
            }
        }
        logger.error("status:$status")
        logger.error("statusCode:${ex.statusCode}")
        logger.error("message:${ex.exceptionMessage}")
        logger.error("data:${ex.data.toString()}")

        return DTOResponseError(ex.statusCode.code, ex.exceptionMessage, ex.user, ex.data).response(
            status
        ) as ResponseEntity<Any>

    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        val result: BindingResult = ex.bindingResult
        var errorMessage = "The following error has occurred:\n"
        result.fieldErrors.forEach { errorMessage += "${it.field}: ${it.defaultMessage}\n" }

        return DTOResponseError(
            ManagementExceptionCode.REQUEST_INVALID_PARAMETER.code, errorMessage, null, null
        ).response(HttpStatus.BAD_REQUEST) as ResponseEntity<Any>
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        var errorMessage = "Incomplete parameters, please check spec"
        return DTOResponseError(
            ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER.code, errorMessage,null , null
        ).response(HttpStatus.BAD_REQUEST) as ResponseEntity<Any>
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        var errorMessage = "Missing parameters, please check parameters"
        return DTOResponseError(
            ManagementExceptionCode.REQUEST_MISSING_REQUEST_PARAMETER.code, errorMessage, null, null
        ).response(HttpStatus.BAD_REQUEST) as ResponseEntity<Any>
    }
}