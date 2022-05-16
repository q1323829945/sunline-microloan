package cn.sunline.saas.dapr_wrapper.invoke

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

data class RPCPageable(val page: Int = 0, val size: Int = 20)

fun RPCPageable.toPageable(): Pageable {
    if (this.page == null && this.size == null) return Pageable.unpaged()
    return PageRequest.of(this.page?:0, this.size?:0)
}

fun Pageable.toRPCPageable(): RPCPageable {
    return RPCPageable(this.pageNumber, this.pageSize)
}

fun Pageable.toRequestParams(): Map<String, String> {
    return mapOf("size" to this.pageSize.toString(), "page" to this.pageNumber.toString())
}