package cn.sunline.saas.multi_tenant.service

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.model.TestDBModel
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import javax.transaction.Transactional

/**
 * @title: TestMultTenantRepoService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 15:12
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultiTenantRepoServiceTest(@Autowired val testDBModelService: TestDBModelService,@Autowired val tenantDateTime: TenantDateTime) {

    @Autowired
    private lateinit var tenantService: TenantService

    @BeforeAll
    fun `init`() {
        val testTenant1 = Tenant(100, CountryType.GBR, true)
        val testTenant2 = Tenant(200, CountryType.GBR, true)
        tenantService.save(mutableListOf(testTenant1,testTenant2))
    }

    @Test
    @Transactional
    fun `entity listener`() {
        ContextUtil.setTenant("100")
        val actual = testDBModelService.save(TestDBModel(date=tenantDateTime.now().toDate()))

        assertThat(actual.getTenantId()).isEqualTo(100L)
    }

    @Test
    fun `get list with tenant`() {
        ContextUtil.setTenant("200")
        val actual = mutableListOf<TestDBModel>()
        actual.add(TestDBModel(date=tenantDateTime.now().toDate()))
        actual.add(TestDBModel(date=tenantDateTime.now().toDate()))
        actual.add(TestDBModel(date=tenantDateTime.now().toDate()))

        testDBModelService.save(actual)

        val page = testDBModelService.getPageWithTenant(pageable = PageRequest.of(0,15, Sort.unsorted()))

        assertThat(page.content.size).isEqualTo(3)
    }

}