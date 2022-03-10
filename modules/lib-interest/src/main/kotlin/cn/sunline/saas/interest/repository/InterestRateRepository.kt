package cn.sunline.saas.interest.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.interest.model.InterestRate
import org.springframework.data.jpa.repository.Query

interface InterestRateRepository : BaseRepository<InterestRate, Long>{

}