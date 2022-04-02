package cn.sunline.saas.document.template.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.document.template.modules.DocumentTemplateDirectory
import org.springframework.data.jpa.repository.Query

/**
 * @title: DocumentDirectory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 16:16
 */
interface DocumentTemplateDirectoryRepository:BaseRepository<DocumentTemplateDirectory,Long>{
}