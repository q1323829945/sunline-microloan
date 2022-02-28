package cn.sunline.saas.multi_tenant.service

import cn.sunline.saas.multi_tenant.model.TestDBModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * @title: TestMultTenantRepoService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 15:12
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultiTenantRepoServiceTest(@Autowired val testDBModelService: TestDBModelService) {


    @Test
    fun `entity listener`() {
        val actual = testDBModelService.save(TestDBModel())
        assertThat(actual.getTenantId()).isEqualTo(100L)
    }

    @Test
    fun `get list with tenant`() {
        var actual = mutableListOf<TestDBModel>()
        actual.add(TestDBModel())
        actual.add(TestDBModel())
        actual.add(TestDBModel())

        testDBModelService.save(actual)

        val page = testDBModelService.getPageWithTenant(pageable = PageRequest.of(0,15, Sort.unsorted()))

        assertThat(page.content.size).isEqualTo(3)
    }

}