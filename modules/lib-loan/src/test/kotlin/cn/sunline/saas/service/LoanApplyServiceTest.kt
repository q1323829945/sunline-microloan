package cn.sunline.saas.service

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.loan.model.dto.*
import cn.sunline.saas.loan.service.LoanApplyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.service.assembly.DTOLoanAssembly
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanApplyServiceTest{
    @Autowired
    private lateinit var loanApplyService: LoanApplyService

    @Autowired
    private lateinit var sequence: Sequence

    lateinit var newClientData:String
    lateinit var clientData:String
    lateinit var teacherData:String
    lateinit var corporateData:String
    lateinit var kabuhayanData:String

    lateinit var newClientId:String
    lateinit var clientId:String
    lateinit var teacherId:String
    lateinit var corporateId:String
    lateinit var kabuhayanId:String


    @BeforeAll
    fun `loan apply record`(){
        ContextUtil.setTenant("0")

        val dtoNewClientLoan = DTOLoanAssembly.getNewClientLoan(sequence.nextId().toString())
        val newClientLoan = loanApplyService.addNewClientLoan(dtoNewClientLoan)
        Assertions.assertThat(newClientLoan).isNotNull
        newClientData = newClientLoan.data
        newClientId = newClientLoan.applicationId.toString()

        val dtoClientLoan = DTOLoanAssembly.getClientLoan(sequence.nextId().toString())
        val clientLoan = loanApplyService.addClientLoan(dtoClientLoan)
        Assertions.assertThat(clientLoan).isNotNull
        clientData = clientLoan.data
        clientId = clientLoan.applicationId.toString()

        val dtoTeacherLoan = DTOLoanAssembly.getTeacherLoan(sequence.nextId().toString())
        val teacherLoan = loanApplyService.addTeacherLoan(dtoTeacherLoan)
        Assertions.assertThat(teacherLoan).isNotNull
        teacherData = teacherLoan.data
        teacherId = teacherLoan.applicationId.toString()

        val dtoCorporateLoan = DTOLoanAssembly.getCorporateLoan(sequence.nextId().toString())
        val corporateLoan = loanApplyService.addCorporateLoan(dtoCorporateLoan)
        Assertions.assertThat(corporateLoan).isNotNull
        corporateData = corporateLoan.data
        corporateId = corporateLoan.applicationId.toString()

        val dtoKabuhayanLoan = DTOLoanAssembly.getKabuhayanLoan(sequence.nextId().toString())
        val kabuhayanLoan = loanApplyService.addKabuhayanLoan(dtoKabuhayanLoan)
        Assertions.assertThat(kabuhayanLoan).isNotNull
        kabuhayanData = kabuhayanLoan.data
        kabuhayanId = kabuhayanLoan.applicationId.toString()

    }

    @Test
    fun `update new client loan apply`(){
        val loanApply = loanApplyService.updateNewClientLoan(newClientData)
        Assertions.assertThat(loanApply).isNotNull
    }

    @Test
    fun `update client loan apply`(){
        val loanApply = loanApplyService.updateClientLoan(clientData)
        Assertions.assertThat(loanApply).isNotNull
    }

    @Test
    fun `update teacher loan apply`(){
        val loanApply = loanApplyService.updateTeacherLoan(teacherData)
        Assertions.assertThat(loanApply).isNotNull
    }

    @Test
    fun `update corporate loan apply`(){
        val loanApply = loanApplyService.updateCorporateLoan(corporateData)
        Assertions.assertThat(loanApply).isNotNull
    }

    @Test
    fun `update kabuhayan loan apply`(){
        val loanApply = loanApplyService.updateKabuhayanLoan(kabuhayanData)
        Assertions.assertThat(loanApply).isNotNull


    }

    @Test
    fun `get loan apply details`(){
        val channel = DTOChannelInformationView(code = "code", name = "name")

        val newClientDetails = loanApplyService.getLoanApplyDetails(newClientId.toLong())
        Assertions.assertThat(newClientDetails).hasSameClassAs(DTONewClientLoanView(applicationId = "123", channel = channel))

        val clientDetails = loanApplyService.getLoanApplyDetails(clientId.toLong())
        Assertions.assertThat(clientDetails).hasSameClassAs(DTOClientLoanView(applicationId = "123", channel = channel))

        val teacherDetails = loanApplyService.getLoanApplyDetails(teacherId.toLong())
        Assertions.assertThat(teacherDetails).hasSameClassAs(DTOTeacherLoanView(applicationId = "123", channel = channel))

        val corporateDetails = loanApplyService.getLoanApplyDetails(corporateId.toLong())
        Assertions.assertThat(corporateDetails).hasSameClassAs(DTOCorporateLoanView(applicationId = "123", channel = channel))

        val kabuhayanDetails = loanApplyService.getLoanApplyDetails(kabuhayanId.toLong())
        Assertions.assertThat(kabuhayanDetails).hasSameClassAs(DTOKabuhayanLoanView(applicationId = "123", channel = channel))
    }
}