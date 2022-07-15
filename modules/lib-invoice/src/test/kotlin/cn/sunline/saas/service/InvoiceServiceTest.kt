package cn.sunline.saas.service

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InvoiceServiceTest {
    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var tenantService: TenantService

    var id = 0L

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")

        tenantService.save(
            Tenant(
                id = 123,
                country = CountryType.CHN,
            )
        )

        val invoice = invoiceService.initiateLoanInvoice(
            dtoLoanInvoices = mutableListOf(
                DTOLoanInvoice(
                    period = 1,
                    invoicePeriodFromDate = "20220611",
                    invoicePeriodToDate = "20220612",
                    invoicee = 1,
                    principal = BigDecimal(500),
                    interest = BigDecimal(1.5),
                    fee = BigDecimal(1.5),
                    agreementId = 1,
                    invoiceStatus = InvoiceStatus.INITIATE,

                ),
                DTOLoanInvoice(
                    period = 1,
                    invoicePeriodFromDate = "20220611",
                    invoicePeriodToDate = "20220612",
                    invoicee = 1,
                    principal = BigDecimal(10000),
                    interest = BigDecimal(1.5),
                    fee = BigDecimal(1.5),
                    agreementId = 1,
                    invoiceStatus = InvoiceStatus.FINISHED,
                )
            )
        )
        Assertions.assertThat(invoice).isNotNull


        id = invoice.first().id
    }

    @Test
    fun listInvoiceByAgreementId(){
        val invoices = invoiceService.listInvoiceByAgreementId(1, Pageable.unpaged())
        Assertions.assertThat(invoices.size).isEqualTo(2)
    }

    @Test
    @Transactional
    fun calculateRepaymentAmountById(){
        val amount = invoiceService.calculateRepaymentAmountById(id)

        Assertions.assertThat(amount).isEqualTo(BigDecimal(503).setScale(2))
    }

    @Test
    @Transactional
    fun calculateRepaymentAmount(){
        val invoices = invoiceService.listInvoiceByAgreementId(1, Pageable.unpaged())
        val amount = invoiceService.calculateRepaymentAmount(invoices.content[0])

        Assertions.assertThat(amount).isEqualTo(BigDecimal(503).setScale(2))
    }

    @Test
    @Transactional
    fun repayInvoice(){
        val invoices = invoiceService.listInvoiceByAgreementId(1, Pageable.unpaged())
        val map = invoiceService.repayInvoice(
            amount = BigDecimal(10),
            invoice =invoices.content[0],
            graceDays = 1,
            today = DateTime(),
        )

        Assertions.assertThat(map[InvoiceAmountType.INTEREST]).isEqualTo(BigDecimal(1.50).setScale(2))
        Assertions.assertThat(map[InvoiceAmountType.PRINCIPAL]).isEqualTo(BigDecimal(0))
        Assertions.assertThat(map[InvoiceAmountType.FEE]).isEqualTo(BigDecimal(0))

    }


    @Test
    @Transactional
    fun overdueInvoice(){
        val invoices = invoiceService.listInvoiceByAgreementId(1, Pageable.unpaged())
        val amount = invoiceService.overdueInvoice(invoices.content,1, DateTime.now())

        Assertions.assertThat(amount).isGreaterThan(BigDecimal(0))
    }

    @Test
    @Transactional
    fun listInvoiceByInvoiceeAndDate(){
        val paged = invoiceService.listInvoiceByInvoiceeAndDate(invoicee = 1,invoiceStartDate = null,invoiceEndDate = null)

        Assertions.assertThat(paged.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun retrieveCurrentInvoices(){
        val paged = invoiceService.retrieveCurrentInvoices(invoicee = 1, invoiceStatus = InvoiceStatus.INITIATE)

        Assertions.assertThat(paged.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun getInvokesPaged(){
        val paged = invoiceService.getInvokesPaged(customerId = null, invoiceStatus = null, PageRequest.of(0,20))

        Assertions.assertThat(paged.content.size).isEqualTo(2)
    }
}