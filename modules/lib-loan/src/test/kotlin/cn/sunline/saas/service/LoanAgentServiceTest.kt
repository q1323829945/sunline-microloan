package cn.sunline.saas.service

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.loan.model.enum.LoanType
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.service.assembly.DTOLoanAssembly
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanAgentServiceTest {
    @Autowired
    private lateinit var loanAgentService: LoanAgentService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    lateinit var corporateId:String
    lateinit var individualId:String

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("0")

        val dtoCorporate = DTOLoanAssembly.getLoanAgent(LoanType.CORPORATE)
        val corporate = loanAgentService.addOne(objectMapper.writeValueAsString(dtoCorporate))
        Assertions.assertThat(corporate).isNotNull
        corporateId= corporate.applicationId.toString()

        val dtoIndividual = DTOLoanAssembly.getLoanAgent(LoanType.INDIVIDUAL)
        val individual = loanAgentService.addOne(objectMapper.writeValueAsString(dtoIndividual))
        Assertions.assertThat(individual).isNotNull
        individualId = individual.applicationId.toString()
    }

//    @Test
//    fun `set product`(){
//        val productId = 123L
//        val loanAgent = loanAgentService.updateOne(corporateId.toLong(),productId)
//        Assertions.assertThat(loanAgent.productId).isEqualTo(productId)
//    }

    @Test
    fun `get paged`(){
        val paged = loanAgentService.getPaged(pageable = Pageable.unpaged())
        Assertions.assertThat(paged.totalElements).isEqualTo(2)
    }

    @Test
    fun `update status`(){
        val loanAgent = loanAgentService.updateStatus(individualId.toLong(),ApplyStatus.SUBMIT)
        Assertions.assertThat(loanAgent.status).isEqualTo(ApplyStatus.SUBMIT)
    }

    @Test
    fun `get status`(){
        val loanAgent = loanAgentService.getStatus(corporateId.toLong())
        Assertions.assertThat(loanAgent.status).isEqualTo(ApplyStatus.RECORD)
    }

    @Test
    fun `get details`(){
        val loanAgent = loanAgentService.getDetails(corporateId.toLong())
        Assertions.assertThat(loanAgent).hasSameClassAs(DTOLoanAssembly.getLoanAgent(LoanType.CORPORATE))
    }

}