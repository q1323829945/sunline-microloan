package cn.sunline.saas.channel.interest.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.channel.interest.model.InterestRate
import cn.sunline.saas.channel.interest.model.RatePlan
import cn.sunline.saas.channel.interest.model.RatePlanType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

/**
 * @title: RatePlanServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 13:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatePlanServiceTest (@Autowired val ratePlanService: RatePlanService){

    var id = 0L

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")

        val rates: MutableList<InterestRate> = mutableListOf()
        val ratePlanId = 1L
        val rate1 = InterestRate(1, LoanTermType.ONE_YEAR, BigDecimal(7.5),ratePlanId)
        val rate2 = InterestRate(2,LoanTermType.SIX_MONTHS, BigDecimal(6.5),ratePlanId)
        val rate3 = InterestRate(3,LoanTermType.THREE_MONTHS, BigDecimal(3.5),ratePlanId)

        rates.add(rate1)
        rates.add(rate2)
        rates.add(rate3)

        val ratePlan = RatePlan(ratePlanId,"Test Rate Plan",RatePlanType.STANDARD,rates)
        val actual = ratePlanService.save(ratePlan)

        assertThat(actual).isNotNull

        id = actual.id!!
    }

    @Test
    fun `update rate plan`(){
        val oldOne = ratePlanService.getOne(id)

        assertThat(oldOne).isNotNull


        val rates: MutableList<InterestRate> = mutableListOf()
        val ratePlanId = 1L
        val rate1 = InterestRate(1, LoanTermType.ONE_YEAR, BigDecimal(7.5),ratePlanId)
        val rate2 = InterestRate(2,LoanTermType.SIX_MONTHS, BigDecimal(6.5),ratePlanId)
        val rate3 = InterestRate(3,LoanTermType.THREE_MONTHS, BigDecimal(3.5),ratePlanId)
        val rate4 = InterestRate(4,LoanTermType.ONE_YEAR, BigDecimal(2),ratePlanId)

        rates.add(rate1)
        rates.add(rate2)
        rates.add(rate3)
        rates.add(rate4)

        val newOne = RatePlan(ratePlanId,"Test Rate Plan2",RatePlanType.STANDARD,rates)


        val actual = ratePlanService.updateOne(oldOne!!,newOne)

        assertThat(actual).isNotNull

        assertThat(actual.rates.size).isEqualTo(4)
    }

    @Test
    fun `find by type`(){
        val actual = ratePlanService.findByType(RatePlanType.STANDARD)

        assertThat(actual).isNotNull
    }
}