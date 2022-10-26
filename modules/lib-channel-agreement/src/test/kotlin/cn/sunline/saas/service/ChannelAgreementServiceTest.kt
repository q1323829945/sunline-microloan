package cn.sunline.saas.service

import cn.sunline.saas.channel.agreement.service.ChannelAgreementService
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ChannelAgreementServiceTest(
    @Autowired private val channelAgreementService: ChannelAgreementService
//    @Autowired private val tenantDateTime: TenantDateTime
) {

//    @Autowired
//    private lateinit var seq: Sequence
//
//    private var channelAgreementId = 0L
//    private var channelId = 0L
//
//    @BeforeAll
//    fun init() {
//        ContextUtil.setTenant("0")
//        channelId = seq.nextId()
//    }
//
//    @Test
//    @Transactional
//    @Order(1)
//    fun `save channel agreement`() {
//
//        val now = DateTime(2022, 1, 1, 0, 0, 0)//tenantDateTime.now()
//
//        val list = mutableListOf<DTOChannelArrangementAdd>()
//        list += DTOChannelArrangementAdd(
//            commissionType = CommissionType.LOAN_APPLICATION,
//            commissionMethodType = CommissionMethodType.APPLY_COUNT_FIX_AMOUNT,
//            commissionAmount = null,
//            commissionRatio = BigDecimal("0.3"),
//            commissionAmountRangeType = CommissionAmountRangeType.DEFAULT,
//            commissionCountRangeType = null
//        )
//        val channelAgreement = channelAgreementService.registered(
//            DTOChannelAgreementAdd(
//                channelId = channelId,
//                agreementType = AgreementType.COMMISSION_SALE,
//                fromDateTime = now.toString(),
//                toDateTime = now.plusYears(1).toString(),
//                channelCommissionArrangement = list
//            )
//        )
//        Assertions.assertThat(channelAgreement).isNotNull
//
//        channelAgreementId = channelAgreement.channelAgreement.id
//    }
//
//    @Test
//    @Order(2)
//    fun `get channel agreement by agreementId`() {
//        val channelAgreement = channelAgreementService.getDetail(channelAgreementId)
//        Assertions.assertThat(channelAgreement).isNotNull
//    }
//
//    @Test
//    @Order(3)
//    fun `get channel agreement by ChannelId And AgreementType`() {
//        val channelAgreement =
//            channelAgreementService.getOneByChannelIdAndAgreementType(channelId, AgreementType.COMMISSION_SALE)
//        Assertions.assertThat(channelAgreement).isNotNull
//    }
//
//
//    @Test
//    @Order(4)
//    fun `get channel agreement by ChannelId`() {
//        val channelAgreementPage = channelAgreementService.getPageByChannelId(channelId, Pageable.unpaged())
//        Assertions.assertThat(channelAgreementPage).isNotNull
//    }
}