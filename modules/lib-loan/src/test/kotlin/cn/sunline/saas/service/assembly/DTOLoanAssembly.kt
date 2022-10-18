package cn.sunline.saas.service.assembly

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.loan.model.dto.*
import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

object DTOLoanAssembly {
    fun getNewClientLoan(applicationId: String): DTONewClientLoan {
        return DTONewClientLoan(
            applicationId = applicationId,
            cif = CIF(
                number = "",
                type = CIFType.ACCOUNT_OWNER,
            ),
            customerInformation = DTOCustomerInformation(
                name = getBaseName(),
                birthDay = "19991010",
                isBDOCustomer = true,
                bdoProductType = BDOProductType.CARDS
            ),
            contactInformation = ClientContactInformation(
                mobileInformation = getBasePhone(),
                personalEmail = "email@email.com",
                homeAddress = getBaseAddress(),
            ),
            personalInformation = ClientPersonalInformation(
                birthCountry = "birthCountry",
                gender = Gender.FEMALE,
                civilStatus = CivilStatus.ANNULLED,
                citizenship = "citizenship",
                tin = "123456",
            ),
            financialInformation = NewClientFinancialInformation(
                fundsSource = mutableListOf(FundsSource.FIXED_INCOME, FundsSource.BUSINESS_INCOME),
                remittanceCountry = "country",
                primarilyUse = "use",
                natures = mutableListOf(Natures.ACT, Natures.ADV),
                name = "name",
                position = DTOPosition(
                    positionType = PositionType.SELF,
                    positionSubType = PositionSubType.OWNER,
                ),
                grossMonthlyIncome = BigDecimal(100),
                stayAtCurrentHome = getBaseStay(),
                ownership = Ownership.OWNED,
                employmentType = EmploymentType.EMPLOYED,
                employmentSubType = EmploymentSubType.GOVERNMENT,
                stayWithPreviousBusiness = getBaseStay(),
                stayWithCurrentBusiness = getBaseStay(),
                creditCards = mutableListOf(
                    DTOCreditCard(
                        bank = "bank1",
                        lastSixDigits = "123456",
                        year = "1996"
                    ),
                    DTOCreditCard(
                        bank = "bank2",
                        lastSixDigits = "987654",
                        year = "1234"
                    )
                )
            ),
            dataPrivacyConsent = DTOSignature(signature = "123"),
            consentForTheIssuance = DTOSignature(signature = "456"),
            customerUndertaking = DTOSignature(signature = "789"),
            internal = DTOInternal(
                account = "123456",
                openedDate = "20111111",
                residency = Residency.RESIDENT,
                biometrics = mutableListOf(Biometrics.FACE, Biometrics.FINGER),
                rc = RC.H,
                nlds = false,
                id1 = DTOClientID(
                    type = "type1",
                    number = "123456"
                ),
                id2 = DTOClientID(
                    type = "type2",
                    number = "345678"
                ),
                referred = "referred",
                verified = "verified",
                approved = "approved",
                remark = "remark"
            ),
            productInformation = DTOProductInformation(
                productType = PersonalProductType.CREDIT_CARD,
                creditCardDetails = DTOCreditCardDetails(
                    creditCardType = CreditCardType.DINERS_CLUB,
                    creditCardSubType = CreditCardSubType.INTERNATIONAL,
                ),
                details = DTOProductDetails(
                    amount = BigDecimal.TEN,
                    term = LoanTermType.ONE_YEAR,
                    purpose = "purpose"
                ),
            ),
            deliverInformation = DTODeliverInformation(
                deliverAddress = DeliverAddress.HOME,
                deliverEmail = DeliverEmail.PERSONAL,
            ),
            adaInformation = DTOAdaInformation(
                newAccount = false,
                accountInformation = DTOAccountInformation(
                    bdoAccount = "123456",
                    primarySignatory = "primarySignatory",
                    secondarySignatory = "secondarySignatory"
                ),
            ),
            questionnaires = mutableListOf(
                DTOQuestionnaire(
                    question = "Q1",
                    answer = true
                ),
                DTOQuestionnaire(
                    question = "Q2",
                    answer = true
                )
            ),
            signature = "s1",
            channel = getChannel()
        )
    }

    fun getClientLoan(applicationId: String): DTOClientLoan {
        return DTOClientLoan(
            applicationId = applicationId,
            cifNumber = "123",
            customerInformation = DTOCustomerInformation(
                name = getBaseName(),
                birthDay = "19991010",
                isBDOCustomer = true,
                bdoProductType = BDOProductType.CARDS
            ),
            financialInformation = ClientFinancialInformation(
                stayAtCurrentHome = getBaseStay(),
                ownership = Ownership.OWNED,
                employmentType = EmploymentType.EMPLOYED,
                employmentSubType = EmploymentSubType.GOVERNMENT,
                stayWithPreviousBusiness = getBaseStay(),
                stayWithCurrentBusiness = getBaseStay(),
                grossMonthlyIncome = BigDecimal.ONE,
                creditCards = mutableListOf(
                    DTOCreditCard(
                        bank = "bank1",
                        lastSixDigits = "123456",
                        year = "1996"
                    ),
                    DTOCreditCard(
                        bank = "bank2",
                        lastSixDigits = "987654",
                        year = "1234"
                    )
                )
            ),
            productInformation = DTOProductInformation(
                productType = PersonalProductType.CREDIT_CARD,
                creditCardDetails = DTOCreditCardDetails(
                    creditCardType = CreditCardType.DINERS_CLUB,
                    creditCardSubType = CreditCardSubType.INTERNATIONAL,
                ),
                details = DTOProductDetails(
                    amount = BigDecimal.TEN,
                    term = LoanTermType.ONE_YEAR,
                    purpose = "purpose"
                ),
            ),
            deliverInformation = DTODeliverInformation(
                deliverAddress = DeliverAddress.HOME,
                deliverEmail = DeliverEmail.PERSONAL,
            ),
            adaInformation = DTOAdaInformation(
                newAccount = false,
                accountInformation = DTOAccountInformation(
                    bdoAccount = "123456",
                    primarySignatory = "primarySignatory",
                    secondarySignatory = "secondarySignatory"
                ),
            ),
            questionnaires = mutableListOf(
                DTOQuestionnaire(
                    question = "Q1",
                    answer = true
                ),
                DTOQuestionnaire(
                    question = "Q2",
                    answer = true
                )
            ),
            signature = "s1",
            channel = getChannel()
        )
    }

    fun getTeacherLoan(applicationId: String): DTOTeacherLoan {
        return DTOTeacherLoan(
            applicationId = applicationId,
            loanType = TeacherLoanType.SALARY_LOAN,
            personalInformation = DTOTeacherPersonalInformation(
                name = getBaseName(),
                gender = Gender.FEMALE,
                birthDay = "20211111",
                citizenship = "citizenship",
                motherMaidenName = getBaseName(),
                civilStatus = CivilStatus.ANNULLED,
                age = "123",
                birthPlace = "birthPlace",
                numberOfDependents = 0,
            ),
            contactInformation = DTOContactInformation(
                mobile = "123456",
                residenceNo = "res",
                officeNo = "asd",
                email = "email@email.email"
            ),
            educationalBackground = DTOEducationalBackground(
                educationalAttainment = EducationalAttainment.HIGH_SCHOOL,
                lastSchoolAttended = "lastSchoolAttended"
            ),
            employmentInformation = DTOEmploymentInformation(
                employer = "employer",
                address = getBaseAddress(),
                designation = "deis",
                industryExpYear = 5,
                depEdRegion = "region",
                depEdDivision = "division",
                depEdStation = "station",
                employmentStatus = EmploymentStatus.REGULAR,
                email = "email@q.2",
            ),
            financialInformation = DTOTeacherFinancialInformation(
                revenueDetails = DTORevenueInformation(
                    sourceOfIncome = mutableListOf(
                        DTOSourceOfIncome(
                            incomeType = IncomeType.SPOUSE_INCOME,
                            source = "source",
                            grossAmount = BigDecimal.ONE,
                            netAmount = BigDecimal.TEN,
                            frequency = "M",
                        ),
                        DTOSourceOfIncome(
                            incomeType = IncomeType.OTHER_SOURCE_OF_INCOME,
                            source = "source",
                            grossAmount = BigDecimal.ONE,
                            netAmount = BigDecimal.TEN,
                            frequency = "M",
                        )
                    ),
                    totalIncome = DTOTotalIncome(
                        grossAmount = BigDecimal.TEN,
                        netAmount = BigDecimal.TEN
                    ),
                ),
                creditCardInformation = DTOCreditCardInformation(
                    creditCard = "card",
                    creditLimit = BigDecimal.TEN,
                ),
                sourceOfFunds = "funds",
                existingBDONetworkBankAccount = "existing",
                existingBDOAccount = "existing",
                otherBankAccount = "existing",
            ),
            spouseInformation = DTOTeacherSpouseInformation(
                name = getBaseName(),
                birthDay = "19961202",
                birthPlace = "place",
                occupation = "occupation",
                mobile = "123456",
                employer = "employer",
                employerAddress = getBaseAddress(),
                employerTel = "123456",
                grossIncome = BigDecimal.TEN,
            ),
            loanInformation = DTOLoanInformation(
                amount = BigDecimal(500),
                term = LoanTermType.ONE_YEAR,
                purpose = mutableListOf(Purpose.EDUCATION.name, Purpose.EMERGENCY.name),
            ),
            disbursalMode = DTODisbursalMode(
                networkBankCASA = "123467",
                casa = "123",
                payrollAccount = "123"
            ),
            characterReferences = mutableListOf(
                DTOCharacterReferences(
                    name = getBaseName(),
                    contact = "123",
                    relationship = "123",
                ),
                DTOCharacterReferences(
                    name = getBaseName(),
                    contact = "456",
                    relationship = "456",
                )
            ),
            channel = getChannel(),
            signature = "123e",
        )
    }

    fun getCorporateLoan(applicationId: String): DTOCorporateLoan {
        return DTOCorporateLoan(
            applicationId = applicationId,
            borrowerType = BorrowerType.CO_BORROWER,
            program = "123",
            referralInformation = DTOReferralInformation(
                branch = "branch",
                developer = "developer",
                referrer = "referrer",
                accountOfficer = "accountOfficer",
                others = "others",
            ),
            borrowerInformation = DTOBorrowerInformation(
                name = "name",
                nature = "nature",
                operatingYears = 50,
                businessType = BusinessType.CORPORATION,
                registration = "123",
                tin = "123",
                sss = "123",
                businessAddress = getBaseAddress(),
                factoryAddress = getBaseAddress(),
            ),
            partnersInformation = mutableListOf(
                DTOPartnersInformation(
                    name = getBaseName(),
                    position = "123",
                    stockRight = "5",
                    birthDay = "20111111",
                ),
                DTOPartnersInformation(
                    name = getBaseName(),
                    position = "123",
                    stockRight = "5",
                    birthDay = "20111111",
                )
            ),
            mortgagorInformation = DTOMortgagorInformation(
                name = getBaseName(),
                birthDay = "20121105",
                birthPlace = "place",
                gender = Gender.FEMALE,
                civilStatus = CivilStatus.ANNULLED,
                citizenship = "citizenship",
                motherName = getBaseName(),
                fatherName = getBaseName(),
                tin = "123",
                sss = "123",
                mobile = "123456",
                paidType = PaidType.POSTPAID,
                residencePhone = getBasePhone(),
                officePhone = getBasePhone(),
                fax = getBasePhone(),
                email = "email",
                presentAddress = getBaseAddress(),
                permanentAddress = getBaseAddress(),
            ),
            spouseInformation = DTOSpouseInformation(
                name = getBaseName(),
                birthPlace = "birthPlace",
            ),
            loanInformation = DTOCorporateLoanInformation(
                amount = BigDecimal.TEN,
                term = LoanTermType.ONE_YEAR,
                fixingPeriod = "123",
                purpose = DTOPurposeInformation(
                    workingCapital = mutableListOf(WorkingCapital.FUNDING_GAP, WorkingCapital.LOAN_TAKE_OUT),
                    capex = mutableListOf(Capex.CAPEX, Capex.LOAN_TAKE_OUT),
                    investment = mutableListOf(Investment.LOAN_TAKE_OUT, Investment.ACQUISITION),
                ),
            ),
            collateralInformation = DTOCollateralInformation(
                propertyAddress = getBaseAddress(),
                presentRegisteredOwner = "123",
                cct = "123",
                contactPerson = "abc",
                contactNumber = "44444",
                collateralType = CollateralType.CONDOMINIUM,
                collateralUse = CollateralUse.COMMERCIAL,
            ),
            financialInformation = DTOCorporateFinancialInformation(
                deposits = mutableListOf(
                    DTODepositsInformation(
                        bank = "bank1",
                        type = "type",
                        account = "123",
                        openedDate = "196611",
                        outstandingBalance = BigDecimal.TEN,
                        depositor = "depositor",
                    ),
                    DTODepositsInformation(
                        bank = "bank2",
                        type = "type",
                        account = "123",
                        openedDate = "196611",
                        outstandingBalance = BigDecimal.TEN,
                        depositor = "depositor",
                    )
                ),
                loans = mutableListOf(
                    DTOLoansInformation(
                        bank = "bank1",
                        type = "type",
                        amount = BigDecimal(112),
                        grantedDate = "199905",
                        maturityDate = "188803",
                        monthlyPayment = BigDecimal(12),
                    ),
                    DTOLoansInformation(
                        bank = "bank12",
                        type = "type",
                        amount = BigDecimal(112),
                        grantedDate = "199905",
                        maturityDate = "188803",
                        monthlyPayment = BigDecimal(12),
                    )
                )
            ),
            tradeReferences = DTOTradeReferences(
                majorCustomers = mutableListOf(
                    DTOMajorCustomerInformation(
                        companyName = "abc",
                        contactName = "abc",
                        contactNumber = "123",
                    ),
                    DTOMajorCustomerInformation(
                        companyName = "abc",
                        contactName = "abc",
                        contactNumber = "123",
                    )
                ),
                majorSuppliers = mutableListOf(
                    DTOMajorSupplierInformation(
                        companyName = "abc",
                        contactName = "abc",
                        contactNumber = "123",
                    ),
                    DTOMajorSupplierInformation(
                        companyName = "abc",
                        contactName = "abc",
                        contactNumber = "123",
                    )
                )
            ),
            undertaking = DTOUndertaking(
                account = "123",
                appraisalFees = BigDecimal(50),
                signature = "123",
            ),
            channel = getChannel(),
        )
    }

    fun getKabuhayanLoan(applicationId: String): DTOKabuhayanLoan {
        return DTOKabuhayanLoan(
            applicationId = applicationId,
            personalInformation = DTOPersonalInformation(
                name = getBaseName(),
                mobile = "123456",
                email = "email"
            ),
            companyInformation = DTOCompanyInformation(
                name = "namme",
                nature = "nature",
                services = "services",
                operatingYears = 1,
                address = getBaseAddress(),
            ),
            loanInformation = DTOLoanInformation(
                amount = BigDecimal.ONE,
                term = LoanTermType.ONE_YEAR,
                purpose = mutableListOf(Purpose.EMERGENCY.name, Purpose.EDUCATION.name),
            ),
            questionnaires = mutableListOf(
                DTOQuestionnaire(
                    question = "Q1",
                    answer = true
                ),
                DTOQuestionnaire(
                    question = "q2",
                    answer = true
                )
            ),
            signature = "signature",
            channel = getChannel(),
        )
    }

    fun getLoanAgent(loanType: LoanType): DTOLoanAgent {
        return DTOLoanAgent(
            seq = "seq1",
            applicationId = null,
            personalInformation = DTOBasePersonalInformation(
                name = getBaseName(),
                gender = Gender.FEMALE,
                birthDay = "20211110",
                tin = "123456",
                sss = "123456",
                citizenship = "citizenship",
                civilStatus = CivilStatus.ANNULLED,
                numberOfDependents = 0,
                spouseInformation = DTOBaseSpouseInformation(
                    name = getBaseName(),
                ),
                educationalBackground = DTOEducationalBackground(
                    educationalAttainment = EducationalAttainment.HIGH_SCHOOL,
                    lastSchoolAttended = "school"
                ),
            ),
            contactInformation = DTOBaseContactInformation(
                mobile = getBasePhone(),
                email = "123456",
                homeLandLine = getBasePhone(),
                address = getBaseAddress(),
                billingContact = DTOBaseBillingContact(
                    name = getBaseName(),
                    mobile = getBasePhone(),
                    email = "123@123.12",
                    homeLandLine = getBasePhone(),
                )
            ),
            employeeInformation = DTOBaseEmployeeInformation(
                employeeType = EmploymentType.EMPLOYED,
                name = "123456",
                address = getBaseAddress(),
                mobile = getBasePhone(),
                email = "email",
                homeLandLine = getBasePhone(),
            ),
            corporateInformation = DTOBaseCorporateInformation(
                name = "1235name",
                nature = "nature",
                businessType = BusinessType.CORPORATION,
                registrationNo = "123456",
                tin = "123456",
                sss = "2132",
                address = getBaseAddress(),
                mobile = getBasePhone(),
                email = "email",
                homeLandLine = getBasePhone(),
            ),
            loanType = loanType,
            fileInformation = mutableListOf(
                DTOBaseFileInformation(
                    key = "file",
                    path = mutableListOf("p1", "p2")
                )
            ),
            signature = "'123",
            //channel = getChannel(),
            agent = DTOAgent(
                null,
                DTOChannelInformation(
                    "11",
                    "11"
                ),
                null
            )
        )
    }


    private fun getBaseName(): DTONameInformation {
        return DTONameInformation(
            lastName = "las",
            firstName = "fis",
            middleName = "mid",
            suffix = "suf",
        )
    }

    private fun getBaseAddress(): DTOAddressInformation {
        return DTOAddressInformation(
            unit = "unit",
            street = "street",
            village = "village",
            city = "city",
            province = "province",
            country = "country",
            zip = "zip",
            ownership = Ownership.OWNED,
            stay = DTOStayInformation(
                year = 5,
                month = 10,
            ),
            contact = DTOPhoneInformation(
                countryCode = "code",
                areaCode = "area",
                number = "123456",
                local = "local",
            ),
        )
    }

    private fun getBaseStay(): DTOStayInformation {
        return DTOStayInformation(
            year = 5,
            month = 10,
        )
    }

    private fun getBasePhone(): DTOPhoneInformation {
        return DTOPhoneInformation(
            countryCode = "code",
            areaCode = "area",
            number = "123456",
            local = "local",
        )
    }

    private fun getChannel(): DTOChannelInformation {
        return DTOChannelInformation(
            code = "code",
            name = "name"
        )
    }
}