package cn.sunline.saas.multi_tenant.service

import cn.sunline.saas.multi_tenant.model.TestDBModel
import cn.sunline.saas.multi_tenant.repository.TestDBModelRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: TestDBModelService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 15:10
 */
@Service
class TestDBModelService(private val testDBModelRepo: TestDBModelRepository) : BaseMultiTenantRepoService<TestDBModel,Long>(testDBModelRepo)