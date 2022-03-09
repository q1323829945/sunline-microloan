package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import org.assertj.core.api.Assertions.assertThat
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


    @Test
    fun `entity save`() {
        var rates: MutableList<InterestRate> = mutableListOf()
        val ratePlanId = 1L
        val rate1 = InterestRate(1,"12M", BigDecimal(7.5),ratePlanId)
        val rate2 = InterestRate(2,"6M", BigDecimal(6.5),ratePlanId)
        val rate3 = InterestRate(3,"3M", BigDecimal(3.5),ratePlanId)

        rates.add(rate1)
        rates.add(rate2)
        rates.add(rate3)

        val ratePlan = RatePlan(ratePlanId,"Test Rate Plan",RatePlanType.STANDARD,rates)
        val actual = ratePlanService.save(ratePlan)


        assertThat(actual).isNotNull
    }
}