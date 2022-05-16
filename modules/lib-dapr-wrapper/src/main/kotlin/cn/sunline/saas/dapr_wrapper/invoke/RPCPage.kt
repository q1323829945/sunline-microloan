package cn.sunline.saas.dapr_wrapper.invoke

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

data class RPCPage<T>(val content: List<T>, val number: Int, val size: Int, val totalElements: Long, val totalPages: Int, val numberOfElements: Int)

fun <T> Page<T>.toRPCPage(): RPCPage<T> {
    return RPCPage(this.content, this.number, this.size, this.totalElements, this.totalPages, this.numberOfElements)
}

fun <T> RPCPage<T>.toPage(): Page<T> {
    return PageImpl(content, PageRequest.of(this.number, this.size), this.totalElements)
}