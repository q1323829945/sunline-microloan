package cn.sunline.saas.loan_apply.service


import cn.sunline.saas.channel.agreement.service.ChannelAgreementService
import cn.sunline.saas.channel.arrangement.component.ChannelCommissionCalculator
import cn.sunline.saas.channel.arrangement.service.ChannelArrangementService
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.global.constant.ProductType.*
import cn.sunline.saas.global.constant.ProductType.CORPORATE
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getApplyLoanSubmit
import cn.sunline.saas.channel.interest.service.InterestRateService
import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.loan.exception.LoanApplyStatusException
import cn.sunline.saas.loan.model.db.LoanApply
import cn.sunline.saas.loan.model.db.LoanAgent
import cn.sunline.saas.loan.model.db.LoanApplyHandle
import cn.sunline.saas.loan.model.dto.DTOLoanAgent
import cn.sunline.saas.loan.model.dto.DTOLoanApplyHandle
import cn.sunline.saas.loan.model.dto.DTOLoanApplyPageView
import cn.sunline.saas.loan.model.dto.DTOQuestionnaire
import cn.sunline.saas.channel.product.model.dto.DTOProductAppView
import cn.sunline.saas.channel.product.service.ProductService
import cn.sunline.saas.loan.service.LoanApplyHandleService
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.assembly.LoanApplyAssembly
import cn.sunline.saas.modules.dto.DTOLoanApplyAuditAdd
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.product.exception.LoanProductNotFoundException
import cn.sunline.saas.satatistics.service.BusinessStatisticsManagerService
import cn.sunline.saas.satatistics.service.CommissionStatisticsManagerService
import cn.sunline.saas.satatistics.service.LoanApplicationStatisticsManagerService
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.services.LoanApplyAuditService
import cn.sunline.saas.channel.statistics.modules.TransactionType
import cn.sunline.saas.channel.statistics.modules.dto.DTOBusinessDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationDetail
import cn.sunline.saas.channel.statistics.services.CommissionDetailService
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.minio.MinioService
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.criteria.Predicate

@Service
class LoanApplyAppService {

    private var logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var loanApplyService: LoanApplyService

    @Autowired
    private lateinit var interestRateService: InterestRateService

    @Autowired
    private lateinit var createScheduler: CreateScheduler

    @Autowired
    private lateinit var loanApplicationStatisticsManagerService: LoanApplicationStatisticsManagerService

    @Autowired
    private lateinit var commissionStatisticsManagerService: CommissionStatisticsManagerService

    @Autowired
    private lateinit var loanAgentService: LoanAgentService

    @Autowired
    private lateinit var loanApplyAuditService: LoanApplyAuditService

    @Autowired
    private lateinit var loanApplyHandleService: LoanApplyHandleService

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var businessStatisticsManagerService: BusinessStatisticsManagerService

    @Autowired
    private lateinit var minioService: MinioService

    @Autowired
    private lateinit var channelCastService: ChannelCastService

    @Autowired
    private lateinit var channelArrangementService: ChannelArrangementService

    @Autowired
    private lateinit var channelAgreementService: ChannelAgreementService

    @Autowired
    private lateinit var commissionDetailService: CommissionDetailService


    @Autowired
    private lateinit var obsApi: ObsApi
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getProduct(productType: ProductType): DTOProductAppView {
        val product =
            productService.getProductByProductType(productType) ?: throw LoanProductNotFoundException("Invalid product")
        val rates = interestRateService.findByRatePlanId(product.ratePlanId.toLong())
        return DTOProductAppView(
            productId = product.id,
            productType = product.productType,
            term = rates.map { it.period },
            questionnaires = product.questionnaires,
        )
    }

    fun updateStatus(applicationId: String, status: ApplyStatus): LoanAgent {
        val loanAgent = loanAgentService.updateStatus(applicationId.toLong(), status)

        saveBusinessStatistic(applicationId)
        syncLoanApplicationStatistics(applicationId)

        return loanAgent
    }


    fun syncLoanApplicationStatistics(applicationId: String) {
        try {
            logger.info("[syncLoanApplicationStatistics]: sync $applicationId statistics start")
            val loanAgent =
                loanAgentService.getOne(applicationId.toLong())
                    ?: throw LoanApplyNotFoundException("Invalid loan apply")

            val channelCast = channelCastService.getChannelCast(loanAgent.channelCode, loanAgent.channelName)
                ?: throw LoanApplyNotFoundException("Invalid loan apply")
            val channelAgreement = channelAgreementService.getPageByChannelId(
                channelCast.id,
                Pageable.unpaged()
            ).content.first()
            val dtoLoanAgent = LoanApplyAssembly.convertToLoanAgent(loanAgent.data)
            loanApplicationStatisticsManagerService.addLoanApplicationDetail(
                DTOLoanApplicationDetail(
                    channelCode = loanAgent.channelCode,
                    channelName = loanAgent.channelName,
                    productId = loanAgent.productId ?: 0,
                    productName = loanAgent.loanApply?.productType?.name ?: "",
                    applicationId = applicationId.toLong(),
                    applyAmount = loanAgent.loanApply?.amount ?: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    approvalAmount = loanAgent.loanApply?.amount ?: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), //TODO new approval amount
                    status = loanAgent.status,
                    currency = dtoLoanAgent.loanInformation?.currency
                )
            )
            loanApplicationStatisticsManagerService.addLoanApplicationStatistics(tenantDateTime.toTenantDateTime(loanAgent.created!!))

            var commissionAmount = BigDecimal.ZERO
            var ratio: BigDecimal? = BigDecimal.ZERO
            val statisticsAmount = loanAgent.loanApply?.amount ?: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            val rangeValues = channelArrangementService.getRangeValuesByChannelAgreementId(
                channelAgreement.id.toLong(),
                Pageable.unpaged()
            )
            rangeValues.forEach { it ->
                commissionAmount = when (it.commissionMethodType) {
                    CommissionMethodType.APPLY_COUNT_FIX_AMOUNT -> {
                        val applyCount =
                            commissionDetailService.getPaged(pageable = Pageable.unpaged()).content.size + 1
                        ratio = null
                        ChannelCommissionCalculator(it.commissionMethodType).calculate(
                            applyCount.toBigDecimal(),
                            rangeValues
                        ) ?: BigDecimal.ZERO
                    }

                    CommissionMethodType.APPROVAL_COUNT_FIX_AMOUNT -> {
                        val approvalCount = commissionDetailService.getListByStatus(ApplyStatus.APPROVALED).size + 1
                        ratio = null
                        ChannelCommissionCalculator(it.commissionMethodType).calculate(
                            approvalCount.toBigDecimal(),
                            rangeValues
                        ) ?: BigDecimal.ZERO
                    }

                    CommissionMethodType.APPLY_AMOUNT_RATIO -> {
                        val applyAmount =
                            commissionDetailService.getPaged(pageable = Pageable.unpaged()).content.sumOf { it.commissionAmount }
                                .add(statisticsAmount)
                        ratio = ChannelCommissionCalculator(it.commissionMethodType).calculate(applyAmount, rangeValues)
                            ?: BigDecimal.ZERO
                        statisticsAmount.multiply(ratio)
                    }

                    CommissionMethodType.APPROVAL_AMOUNT_RATIO -> {
                        val approvalAmount =
                            commissionDetailService.getListByStatus(ApplyStatus.APPROVALED).sumOf { it.commissionAmount }
                                .add(statisticsAmount)
                        ratio = ChannelCommissionCalculator(it.commissionMethodType).calculate(
                            approvalAmount,
                            rangeValues
                        ) ?: BigDecimal.ZERO
                        statisticsAmount.multiply(ratio)
                    }
                }
            }

            commissionStatisticsManagerService.addCommissionDetail(
                DTOCommissionDetail(
                    channelCode = loanAgent.channelCode,
                    channelName = loanAgent.channelName,
                    applicationId = applicationId.toLong(),
                    commissionAmount = commissionAmount,
                    status = loanAgent.status,
                    currency = CurrencyType.USD,
                    ratio = ratio,
                    statisticsAmount = statisticsAmount
                )
            )

            commissionStatisticsManagerService.addCommissionStatistics(tenantDateTime.toTenantDateTime(loanAgent.created!!))
            logger.info("[syncLoanApplicationStatistics]: sync $applicationId statistics end")
        } catch (e: Exception) {
            logger.error("[syncLoanApplicationStatistics]: sync applicationId:$applicationId , error massage : ${e.message}")
            createScheduler.create(ActorType.LOAN_APPLY_STATISTICS, applicationId)
        }

    }

    fun loanApplySubmit(applicationId: String): LoanAgent {
        var loanAgent = loanAgentService.updateStatus(applicationId.toLong(), ApplyStatus.PROCESSING)
        try {
            //TODO: send to Ios
            logger.info("send $applicationId to Ios")

            loanApplyHandleService.saveOne(
                DTOLoanApplyHandle(
                    applicationId = applicationId,
                    supplementDate = tenantDateTime.now().toDate()
                )
            )

            //Simulation failed
            if (ContextUtil.getApplyLoanSubmit()) {
                throw BusinessException("Simulation failed", ManagementExceptionCode.SUBMIT_ERROR)
            }



            loanAgent = loanAgentService.updateStatus(applicationId.toLong(), ApplyStatus.SUBMIT)
        } catch (e: Exception) {
            logger.error("applicationId:$applicationId , error massage : ${e.message}")
            createScheduler.create(ActorType.LOAN_APPLY_SUBMIT, applicationId)
        }

        return loanAgent
    }

    fun loanApplySubmitCallBack(applicationId: String, status: ApplyStatus): LoanAgent {
        return updateStatus(applicationId, status)
    }

    fun loanRecord(data: String): LoanAgent {
        val dtoLoanAgent = LoanApplyAssembly.convertToLoanAgent(data)

        channelCastService.getUniqueChannelCast(dtoLoanAgent.channel.code,dtoLoanAgent.channel.name)?.run {
            ContextUtil.setTenant(this.getTenantId().toString())
        }?:run { ContextUtil.setTenant("1") }


        dtoLoanAgent.fileInformation?.forEach { files ->
            val obsFiles = mutableListOf<String>()
            files.path?.forEach {
                val key = minioService.minioToObs(it, it)
                key?.run {
                    obsFiles.add(this)
                } ?: run {
                    obsFiles.add(it)
                }

            }
            files.path?.run {
                this.clear()
                this.addAll(obsFiles)
            }
        }
        val newData = objectMapper.writeValueAsString(dtoLoanAgent)
        val loanAgent = loanAgentService.addOne(newData)

        createScheduler.create(ActorType.LOAN_APPLY_HANDLE, loanAgent.applicationId.toString())
        return loanAgent
    }

    fun updateLoanApply(productType: ProductType, data: Any): LoanApply {
        val loanApply = when (productType) {
            NEW_CLIENT -> loanApplyService.updateNewClientLoan(objectMapper.writeValueAsString(data))
            CLIENT -> loanApplyService.updateClientLoan(objectMapper.writeValueAsString(data))
            TEACHER -> loanApplyService.updateTeacherLoan(objectMapper.writeValueAsString(data))
            KABUHAYAN -> loanApplyService.updateKabuhayanLoan(objectMapper.writeValueAsString(data))
            CORPORATE -> loanApplyService.updateCorporateLoan(objectMapper.writeValueAsString(data))
        }

        audit(loanApply.applicationId)

        return loanApply
    }

    private fun audit(applicationId: Long) {
        val loanAgent = loanAgentService.getOne(applicationId) ?: throw LoanApplyNotFoundException("Invalid loan apply")
        loanApplyAuditService.addLoanApplyAudit(
            dtoLoanApplyAuditAdd = DTOLoanApplyAuditAdd(
                applicationId = loanAgent.applicationId.toString(),
                name = loanAgent.name,
                productId = loanAgent.productId,
                term = loanAgent.loanApply?.term,
                amount = loanAgent.loanApply?.amount,
                data = loanAgent.data,
                status = loanAgent.status
            )
        )
    }

    fun supplement(applicationId: String, username: String) {
        val loanAgent =
            loanAgentService.getOne(applicationId.toLong()) ?: throw LoanApplyNotFoundException("Invalid loan apply")

        if (loanAgent.status != ApplyStatus.RECORD) {
            throw throw LoanApplyStatusException("The loan apply has been supplement!")
        }
        loanApplyHandleService.saveOne(
            DTOLoanApplyHandle(
                applicationId = applicationId,
                supplement = username
            )
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addProduct(applicationId: String, productId: String) {
        val loanAgent = loanAgentService.updateOne(applicationId.toLong(), productId.toLong())
        val product = productService.getProduct(productId.toLong())

        val dtoLoanAgent = LoanApplyAssembly.convertToLoanAgent(loanAgent.data)
        dtoLoanAgent.applicationId = loanAgent.applicationId.toString()

        val questionnaires = product.questionnaires?.run { objectMapper.convertValue<List<DTOQuestionnaire>>(this) }
        val loanApply = when (product.productType) {
            NEW_CLIENT -> loanApplyService.addNewClientLoan(
                LoanApplyAssembly.convertToNewClientLoan(
                    dtoLoanAgent,
                    questionnaires
                )
            )

            CLIENT -> loanApplyService.addClientLoan(
                LoanApplyAssembly.convertToClientLoan(
                    dtoLoanAgent,
                    questionnaires
                )
            )

            TEACHER -> loanApplyService.addTeacherLoan(LoanApplyAssembly.convertToTeacherLoan(dtoLoanAgent))
            KABUHAYAN -> loanApplyService.addKabuhayanLoan(
                LoanApplyAssembly.convertToKabuhayanLoan(
                    dtoLoanAgent,
                    questionnaires
                )
            )

            CORPORATE -> loanApplyService.addCorporateLoan(LoanApplyAssembly.convertToCorporateLoan(dtoLoanAgent))
        }

        audit(loanApply.applicationId)
    }

    fun getLoanAgentDetail(applicationId: String): DTOLoanAgent {
        val loanAgent = loanAgentService.getDetails(applicationId.toLong())
        loanAgent.productId?.run {
            val product = productService.getOne(this.toLong())
            product?.run {
                loanAgent.productName = this.name
            }
        }

        return loanAgent
    }

    fun getPaged(
        productType: ProductType?, status: ApplyStatus?, username: String?, pageable: Pageable
    ): Page<DTOLoanApplyPageView> {
        val paged = loanAgentService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            status?.run { predicates.add(criteriaBuilder.equal(root.get<ApplyStatus>("status"), status)) }

            productType?.run {
                val loanApplyHandleTable = root.join<LoanAgent, LoanApply>("loanApply")
                predicates.add(criteriaBuilder.equal(loanApplyHandleTable.get<ProductType>("productType"), productType))
            }
            username?.run {
                val loanApplyHandleTable = root.join<LoanAgent, LoanApplyHandle>("handle")
                predicates.add(criteriaBuilder.equal(loanApplyHandleTable.get<String>("supplement"), username))
            }

            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.by(Sort.Order.desc("created"))))

        return paged.map {
            val product = it.productId?.run { productService.getOne(this) }
            val loanAgentData = LoanApplyAssembly.convertToLoanAgent(it.data)
            DTOLoanApplyPageView(
                applicationId = it.applicationId.toString(),
                name = it.name,
                amount = it.loanApply?.amount?:run { loanAgentData.loanInformation?.amount?.run { BigDecimal(this) } },
                productName = product?.name,
                productType = it.loanApply?.productType,
                term = it.loanApply?.term?:run { loanAgentData.loanInformation?.term },
                date = it.created?.run { tenantDateTime.toTenantDateTime(this).toString() },
                status = it.status,
                channelCode = it.channelCode,
                channelName = it.channelName,
                supplement = it.handle?.supplement
            )
        }
    }


    fun saveBusinessStatistic(applicationId: String) {
        try {
            logger.info("[saveBusinessDetailStatistic]: save $applicationId business detail statistics start")

            val loanAgent =
                loanAgentService.getOne(applicationId.toLong())
                    ?: throw LoanApplyNotFoundException("Invalid loan apply")
            val dtoLoanAgent = LoanApplyAssembly.convertToLoanAgent(loanAgent.data)

            businessStatisticsManagerService.addBusinessDetail(
                DTOBusinessDetail(
                    agreementId = applicationId.toLong(),
                    customerId = 1L, //TODO get channel
                    amount = loanAgent.loanApply?.amount ?: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    currency = dtoLoanAgent.loanInformation?.currency,
                    transactionType = TransactionType.PAYMENT
                )
            )

            businessStatisticsManagerService.addBusinessStatistics()

            logger.info("[saveBusinessDetailStatistic]: save $applicationId  business detail statistics end")
        } catch (e: Exception) {
            logger.error("[saveBusinessDetailStatistic]: save applicationId:$applicationId  business detail statistics, error massage : ${e.message}")
            createScheduler.create(ActorType.LOAN_APPLY_STATISTICS, applicationId)
        }
    }

    fun test1(){
        loanAgentService.getPageWithTenant(null, Pageable.unpaged() ).content.forEach {
            syncLoanApplicationStatistics(it.applicationId.toString())
        }
    }
}