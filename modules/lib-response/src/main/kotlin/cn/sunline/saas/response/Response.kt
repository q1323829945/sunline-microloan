package cn.sunline.saas.response

import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletResponse

data class DTOResponseSuccess<T>(var data: T? = null, var code: Int = 0)

data class DTOResponseError(var code: Int, var exceptionMessage: String? = null, var message: String? = null, var data: Any? = null)

data class DTOPagination(var size: Int? = 0, var page: Int? = 0, var total: Long? = 0)

data class DTOPagedResponseSuccess(
        var data: List<Any> = listOf(),
        var paged: DTOPagination? = DTOPagination()) {
    constructor(page: Page<Any>): this(data = page.content) {
        paged = DTOPagination(page.size, page.number, page.totalElements)
    }
}

fun <T> DTOResponseSuccess<T>.response(): ResponseEntity<DTOResponseSuccess<T>> {
    return ResponseEntity.ok(this)
}

fun DTOResponseError.response(): ResponseEntity<DTOResponseError> {
    return ResponseEntity.badRequest().body(this)
}

fun DTOPagedResponseSuccess.response(): ResponseEntity<DTOPagedResponseSuccess> {
    return ResponseEntity.ok(this)
}

fun HttpServletResponse.outputErrorStream(message: String?, code: Int) {
    this.contentType = MediaType.APPLICATION_JSON_VALUE
    this.status = HttpServletResponse.SC_BAD_REQUEST

    this.outputStream.println("{\"message\": \"$message\", \"code\": ${code} }")
}