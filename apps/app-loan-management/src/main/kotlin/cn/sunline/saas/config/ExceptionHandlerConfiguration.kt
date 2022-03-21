package cn.sunline.saas.config

import cn.sunline.saas.exceptions.BaseException
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.UploadException
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
    @ExceptionHandler(value = [(ManagementException::class)])
    fun handleManagementException(ex: ManagementException): ResponseEntity<DTOResponseError> {
        return DTOResponseError(ex.statusCode.code, ex.exceptionMessage, ex.message, ex.data).response()
    }

    @ExceptionHandler(value = [(UploadException::class)])
    fun handleUploadException(ex:BaseException):ResponseEntity<DTOResponseError>{
        return DTOResponseError(ex.statusCode.code,ex.exceptionMessage,ex.message,ex.data).response()
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val result: BindingResult = ex.bindingResult
        var errorMessage = "The following error has occurred:\n"
        result.fieldErrors.forEach { errorMessage += "${it.field}: ${it.defaultMessage}\n" }

        return DTOResponseError(ManagementExceptionCode.REQUEST_INVALID_PARAMETER.code, "", errorMessage, null).response() as ResponseEntity<Any>
    }

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        var errorMessage = "Incomplete parameters, please check spec"
        return DTOResponseError(ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER.code, "", errorMessage, null).response() as ResponseEntity<Any>
    }

    override fun handleMissingServletRequestParameter(ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        var errorMessage = "Missing parameters, please check parameters"
        return DTOResponseError(ManagementExceptionCode.REQUEST_MISSING_REQUEST_PARAMETER.code, "", errorMessage, null).response() as ResponseEntity<Any>
    }
}