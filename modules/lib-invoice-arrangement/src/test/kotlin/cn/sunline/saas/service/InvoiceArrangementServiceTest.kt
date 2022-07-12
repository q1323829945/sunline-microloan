package cn.sunline.saas.service

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.invoice.arrangement.service.DTOInvoiceArrangement
import cn.sunline.saas.invoice.arrangement.service.InvoiceArrangementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import cn.sunline.saas.seq.Sequence
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InvoiceArrangementServiceTest {

    @Autowired
    private lateinit var invoiceArrangementService: InvoiceArrangementService

    @Autowired
    private lateinit var sequence: Sequence

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")
    }

    @Test
    fun registerInvoiceArrangement(){
        val invoiceArrangement =  invoiceArrangementService.registerInvoiceArrangement(
            agreementId = sequence.nextId(),
            dtoInvoiceArrangement = DTOInvoiceArrangement(
                invoiceDay = 5,
                repaymentDay = 10,
                graceDays = 10,
            )
        )
        Assertions.assertThat(invoiceArrangement).isNotNull
    }
}