package cn.sunline.saas.interest.service

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @title: InterestProductFeatureService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 13:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InterestFeatureServiceTest(
    @Autowired val interestFeatureService: InterestFeatureService,
) {
    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")

        val interestFeature = DTOInterestFeatureAdd(
            interestType = InterestType.FIXED,
            ratePlanId = 1000,
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            adjustFrequency = "NOW",
            overdueInterestRatePercentage = 150,
            basicPoint = null
        )

        val actual = interestFeatureService.register(1,interestFeature)

        assertThat(actual).isNotNull
        assertThat(actual.interest).isNotNull
        assertThat(actual.overdueInterest).isNotNull
        assertThat(actual.interest.adjustFrequency).isEqualTo("NOW")
    }


    @Test
    fun `find by productId`() {
        val interestFeature = interestFeatureService.findByProductId(1)


        assertThat(interestFeature).isNotNull
    }
    @Test
    fun `find by ratePlanId`() {
        val interestFeature = interestFeatureService.findByRatePlanId(1000)


        assertThat(interestFeature).isNotNull
    }

}