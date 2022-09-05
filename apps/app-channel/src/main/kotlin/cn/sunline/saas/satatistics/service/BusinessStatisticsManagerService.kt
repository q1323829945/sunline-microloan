package cn.sunline.saas.satatistics.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.satatistics.service.dto.DTOApiStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOBusinessStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOCustomerStatisticsCount
import cn.sunline.saas.channel.statistics.modules.TransactionType
import cn.sunline.saas.channel.statistics.modules.db.BusinessStatistics
import cn.sunline.saas.channel.statistics.modules.dto.*
import cn.sunline.saas.channel.statistics.services.ApiStatisticsService
import cn.sunline.saas.channel.statistics.services.BusinessDetailService
import cn.sunline.saas.channel.statistics.services.BusinessStatisticsService
import cn.sunline.saas.channel.statistics.services.CustomerStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class BusinessStatisticsManagerService(
    private val tenantDateTime: TenantDateTime
) {
    @Autowired
    private lateinit var businessStatisticsService: BusinessStatisticsService

    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    fun getStatisticsByDate(year:Long,month:Long,day:Long,tenantId:Long): List<DTOBusinessStatisticsCount> {
        val page = businessStatisticsService.getPaged({
            root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),year))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),month))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),day))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"), Frequency.D))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), tenantId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())


        return page.content.groupBy { it.currency }.map { groupBy ->
            DTOBusinessStatisticsCount(
                groupBy.value.sumOf { it.paymentAmount }
                ,groupBy.value.sumOf { it.repaymentAmount }
                ,groupBy.key
                ,tenantId)
        }
    }


    fun addBusinessDetail(dtoBusinessDetail:DTOBusinessDetail){
        businessDetailService.getByApplicationId(dtoBusinessDetail.agreementId.toLong()) ?: run {
            businessDetailService.saveBusinessDetail(dtoBusinessDetail)
        }
    }

    fun addBusinessStatistics() {
        val nowDate = tenantDateTime.now()
        if (nowDate.hourOfDay == 0) {
            //根据租户时区统计数据
            //每日统计
            saveDay(nowDate)
            //每月统计
            if (nowDate.dayOfMonth == 1) {
                saveMonth(nowDate)

                //每年统计
                if (nowDate.monthOfYear == 1) {
                    saveYear(nowDate)
                }
            }
        }
    }

    private fun saveYear(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusYears(1)
        addBusinessStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.Y)
    }

    private fun saveMonth(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusMonths(1)
        addBusinessStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.M)
    }

    private fun saveDay(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusDays(1)
        addBusinessStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.D)
    }

    private fun addBusinessStatistics(dateTime:DateTime,startDate: Date, endDate: Date, frequency: Frequency){
        val customer = businessDetailService.getGroupByBusinessCount(DTOBusinessDetailQueryParams(startDate,endDate))
        customer.forEach {
            val business = checkBusinessExist(it.customerId,dateTime,frequency,it.currency)
            business?:run {
                businessStatisticsService.saveBusinessStatistics(
                    DTOBusinessStatistics(
                        customerId = it.customerId,
                        paymentAmount = it.paymentAmount,
                        repaymentAmount = it.repaymentAmount,
                        currencyType = it.currency,
                        frequency = frequency
                    )
                )
            }
        }
    }

    private fun checkBusinessExist(customerId:Long, dateTime: DateTime, frequency: Frequency, currencyType: CurrencyType): BusinessStatistics?{
        return businessStatisticsService.findByDate(DTOBusinessStatisticsFindParams(customerId,dateTime,frequency,currencyType))
    }

}