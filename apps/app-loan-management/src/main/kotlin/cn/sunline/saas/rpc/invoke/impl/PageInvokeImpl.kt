package cn.sunline.saas.rpc.invoke.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable


class PageInvokeImpl<T> {

    fun rePaged(content: List<T>, pageable: Pageable): Page<T> {
        if (pageable.isUnpaged) {
            return PageImpl(content)
        } else {
            val totalSize = content.size
            val start = if (pageable.pageSize * pageable.pageNumber > totalSize) {
                totalSize
            } else {
                pageable.pageSize * pageable.pageNumber
            }
            val end = if (pageable.pageSize * (pageable.pageNumber + 1) > totalSize) {
                totalSize
            } else {
                pageable.pageSize * (pageable.pageNumber + 1)
            }


            val newContent = content.subList(start, end)
            return PageImpl(newContent, pageable, totalSize.toLong())
        }

    }
}