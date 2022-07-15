package cn.sunline.saas.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAuthority
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PdpaAuthorityServiceTest {
    @Autowired
    private lateinit var pdpaAuthorityService: PdpaAuthorityService

    @BeforeAll
    fun init() {
        ContextUtil.setTenant("12344566")
        pdpaAuthorityService.register()
    }

    @Test
    fun `get authority`(){
        val authority = pdpaAuthorityService.getOne()
        Assertions.assertThat(authority).isNotNull
    }

    @Test
    fun `update authority`(){
        val authority = pdpaAuthorityService.updateOne(
            DTOPdpaAuthority(
                isFingerprint = true,
                isElectronicSignature = true,
                isFaceRecognition = true
            )
        )

        Assertions.assertThat(authority.isElectronicSignature).isTrue
        Assertions.assertThat(authority.isFingerprint).isTrue
        Assertions.assertThat(authority.isFaceRecognition).isTrue

    }
}