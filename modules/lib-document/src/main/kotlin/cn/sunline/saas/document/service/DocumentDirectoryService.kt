package cn.sunline.saas.document.service

import cn.sunline.saas.document.model.DocumentDirectory
import cn.sunline.saas.document.repository.DocumentDirectoryRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: DocumentDirectoryService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 16:27
 */
@Service
class DocumentDirectoryService(private val documentDirectoryRepo: DocumentDirectoryRepository) :
    BaseMultiTenantRepoService<DocumentDirectory, Long>(documentDirectoryRepo)