package cn.sunline.saas.services

import cn.sunline.saas.customer.offer.modules.OwnershipType
import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService.DTOFile
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerOfferServiceTest(
    @Autowired val customerOfferService: CustomerOfferService,
    @Autowired val customerLoanApplyService: CustomerLoanApplyService,
    @Autowired val tenantService: TenantService
) {
    var customerOfferId = 0L

    @BeforeAll
    fun `init`() {
        tenantService.save(
            Tenant(
                id = 12344566,
                country = CountryType.CHN,
            )
        )
        ContextUtil.setTenant("12344566")

        val customerOffer = customerOfferService.initiate(
            DTOCustomerOfferAdd(
                customerOfferProcedure = DTOCustomerOfferProcedureAdd(
                    customerId = 2,
                    customerOfferProcess = "customerOfferProcess",
                    employee = 1
                ),
                product = DTOProductAdd(
                    productId = 1,
                    productName = "productName"
                ),
                pdpa = DTOPDPAAdd(
                    pdpaTemplateId = 1,
                    signature = ""
                )
            )
        )

        customerOfferId = customerOffer.customerOfferId!!.toLong()

        customerLoanApplyService.submit(
            customerOfferId = customerOfferId,
            dtoCustomerOfferLoanAdd = DTOCustomerOfferLoanAdd(
                loan = DTOLoan(
                    amount = "10000",
                    currency = CurrencyType.CNY,
                    term = LoanTermType.ONE_MONTH,
                    local = YesOrNo.Y,
                    employ = "employ",
                ),
                company = DTOCompany(
                    registrationNo = "999999"
                ),
                contact = DTOContact(
                    contacts = "contacts",
                    contactNRIC = "8888",
                    mobileArea = "CN",
                    mobileNumber = "9999999",
                    email = "test@test.test",
                ),
                detail = DTODetail(
                    name = "name",
                    registrationNo = "888888",
                    address = "address",
                    businessType = "businessType",
                    contactAddress = "contactAddress",
                    businessPremiseType = OwnershipType.OWNED,
                    businessFocused = 1,
                ),
                guarantor = DTOGuarantor(
                    primaryGuarantor = "p1",
                    guarantors = mutableListOf(
                        DTOGuarantors(
                            name = "p1",
                            nric = "77777",
                            nationality = CountryType.CHN,
                            mobileArea = "CN",
                            mobileNumber = "77777",
                            email = "email",
                            occupation = "occupation",
                            industryExpYear = 1,
                            manageExpYear = 1,
                            residenceType = "residenceType",
                            residenceOwnership = OwnershipType.OWNED,
                        )
                    )
                ),
                financial = DTOFinancial(
                    lastestYearRevenus = "lastestYearRevenus",
                    mainAccountWithOurBank = YesOrNo.Y,
                    outLoanNotWithOutBank = YesOrNo.Y,
                ),
                uploadDocument = mutableListOf(
                    DTOUploadDocument(
                        documentTemplateId = "1",
                        file = null
                    )
                ),
                kyc = DTOKyc(
                    businessInBlackListArea = YesOrNo.Y,
                    businessPlanInBlackListArea = YesOrNo.Y,
                    businessOrPartnerSanctioned = YesOrNo.Y,
                    relationInBlackListArea = YesOrNo.Y,
                    repaymentSourceInBlackListArea = YesOrNo.Y,
                    representsNeutrality = YesOrNo.Y,
                    representsNeutralityShared = YesOrNo.Y,
                    familiarWithBusiness = "familiarWithBusiness"
                ),
                referenceAccount = DTOReferenceAccount(
                    account = "77788899",
                    accountBank = "7788银行"
                )
            ),
            dtoFile = mutableListOf(
                DTOFile(
                    originalFilename = "2/${customerOfferId}/123.jpg",
                    inputStream = FileInputStream(File("src\\test\\resources\\file\\123.JPG"))
                )
            )
        )
    }

    @Test
    @Transactional
    fun `get customerOffer paged`(){
        val paged = customerOfferService.getCustomerOfferPaged(null,null,null, Pageable.ofSize(20))
        Assertions.assertThat(paged.content.size).isEqualTo(1)
    }



    @Test
    @Transactional
    fun `get customer loan apply `(){
        val loanApply = customerLoanApplyService.retrieve(customerOfferId)

        Assertions.assertThat(loanApply).isNotNull
    }
    @Test
    @Transactional
    fun `update  customer loan apply `(){
        customerLoanApplyService.update(
            customerOfferId = customerOfferId,
            dtoCustomerOfferLoanChange = DTOCustomerOfferLoanChange(
                loan = DTOLoan(
                    amount = "10000",
                    currency = CurrencyType.CNY,
                    term = LoanTermType.ONE_MONTH,
                    local = YesOrNo.Y,
                    employ = "employ",
                ),
                company = DTOCompany(
                    registrationNo = "999999"
                ),
                contact = DTOContact(
                    contacts = "contacts",
                    contactNRIC = "8888",
                    mobileArea = "CN",
                    mobileNumber = "9999999",
                    email = "test@test.test",
                ),
                detail = DTODetail(
                    name = "name",
                    registrationNo = "888888",
                    address = "address",
                    businessType = "businessType",
                    contactAddress = "contactAddress",
                    businessPremiseType = OwnershipType.OWNED,
                    businessFocused = 1,
                ),
                guarantor = DTOGuarantor(
                    primaryGuarantor = "p1",
                    guarantors = mutableListOf(
                        DTOGuarantors(
                            name = "p1",
                            nric = "77777",
                            nationality = CountryType.CHN,
                            mobileArea = "CN",
                            mobileNumber = "77777",
                            email = "email",
                            occupation = "occupation",
                            industryExpYear = 1,
                            manageExpYear = 1,
                            residenceType = "residenceType",
                            residenceOwnership = OwnershipType.OWNED,
                        )
                    )
                ),
                financial = DTOFinancial(
                    lastestYearRevenus = "lastestYearRevenus",
                    mainAccountWithOurBank = YesOrNo.Y,
                    outLoanNotWithOutBank = YesOrNo.Y,
                ),
                uploadDocument = mutableListOf(
                    DTOUploadDocument(
                        documentTemplateId = "1",
                        file = null
                    )
                ),
                kyc = DTOKyc(
                    businessInBlackListArea = YesOrNo.Y,
                    businessPlanInBlackListArea = YesOrNo.Y,
                    businessOrPartnerSanctioned = YesOrNo.Y,
                    relationInBlackListArea = YesOrNo.Y,
                    repaymentSourceInBlackListArea = YesOrNo.Y,
                    representsNeutrality = YesOrNo.Y,
                    representsNeutralityShared = YesOrNo.Y,
                    familiarWithBusiness = "familiarWithBusiness"
                ),
                referenceAccount = DTOReferenceAccount(
                    account = "77788899",
                    accountBank = "7788银行"
                )
            ),
            dtoFile = mutableListOf(
                DTOFile(
                    originalFilename = "2/${customerOfferId}/123.jpg",
                    inputStream = FileInputStream(File("src\\test\\resources\\file\\123.JPG"))
                )
            )
        )
    }

}