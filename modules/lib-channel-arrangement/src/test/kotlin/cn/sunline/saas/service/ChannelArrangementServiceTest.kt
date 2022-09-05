package cn.sunline.saas.service

import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.arrangement.service.ChannelArrangementService
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.seq.Sequence
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ChannelArrangementServiceTest(@Autowired private val channelArrangementService: ChannelArrangementService) {

    @Autowired
    private lateinit var seq: Sequence

    private var channelAgreementId = 0L

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("0")
        channelAgreementId = seq.nextId()
    }

    @Test
    @Order(1)
    fun `save channel arrangement`() {
        val dtoChannelArrangementAdd = DTOChannelArrangementAdd(
            commissionType = CommissionType.LOANAPPLICATION,
            commissionMethodType = CommissionMethodType.RATIO,
            commissionAmount = null,
            commissionRatio = BigDecimal("0.3")
        )
        val actual = channelArrangementService.registered(channelAgreementId,dtoChannelArrangementAdd)

        Assertions.assertThat(actual).isNotNull
    }

    @Test
    @Order(2)
    fun `get channel arrangement`() {
        val organisation = channelArrangementService.getOneByChannelId(channelAgreementId)

        Assertions.assertThat(organisation).isNotNull
    }
}