package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import org.assertj.core.api.Assertions.assertThat
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


    @Test
    fun `entity save`() {
        val interestFeature = DTOInterestFeatureAdd(
            interestType = InterestType.FIXED,
            ratePlanId = 1000,
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            adjustFrequency = "NOW",
            overdueInterestRatePercentage = 150
        )



        val actual = interestFeatureService.register(1,interestFeature)

        assertThat(actual).isNotNull
        assertThat(actual.interest).isNotNull
        assertThat(actual.overdueInterest).isNotNull

        assertThat(actual.interest.adjustFrequency).isEqualTo("NOW")
    }
}