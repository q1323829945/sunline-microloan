package cn.sunline.saas.customer.offer.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.customer.offer.modules.db.CustomerOffer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CustomerOfferRepository: BaseRepository<CustomerOffer, Long>{
    @Query(value = "select co.id as customerOfferId\n" +
            "       ,cla.amount as amount\n" +
            "       ,co.`datetime` as datetime \n" +
            "       ,lp.name as productName\n" +
            "       ,co.status as status\n" +
            "  from customer_offer co \n" +
            "  left join customer_loan_apply cla \n" +
            "    on co.id = cla.customer_offer_id \n" +
            "  left join loan_product lp \n" +
            "    on co.product_id = lp.id \n" +
            " where co.customer_id = :customerId",
    countQuery = "select count(1)\n" +
            "  from customer_offer co \n" +
            " where co.customer_id = :customerId", nativeQuery = true)
    fun getCustomerOfferPaged(@Param("customerId")customerId:Long,pageable: Pageable):Page<Map<String,Any>>
}