package cn.sunline.saas.document.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.document.model.Document

/**
 * @title: Document
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 16:17
 */
interface DocumentRepository : BaseRepository<Document, Long> {

    fun findByDirectoryId(directoryId: Long): MutableList<Document>?
}