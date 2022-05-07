package cn.sunline.saas.customer.offer.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.customer.offer.modules.db.CustomerOffer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CustomerOfferRepository: BaseRepository<CustomerOffer, Long>{
}