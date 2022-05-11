package cn.sunline.saas.multi_tenant.services

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.io.Serializable
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

/**
 * @title: MultiTenantRepoServiceExtension
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/24 15:13
 */
abstract class BaseMultiTenantRepoService<T, ID : Serializable>(
    private val baseRepository: BaseRepository<T, ID>
) :
    BaseRepoService<T, ID>(baseRepository) {

    fun getPageWithTenant(specification: Specification<T>? = null, pageable: Pageable): Page<T> {
        val tenantSpecification: Specification<T> = Specification { root: Root<T>, _, criteriaBuilder ->
            val path: Expression<Long> = root.get("tenantId")
            val predicate = criteriaBuilder.equal(path, ContextUtil.getTenant())

            criteriaBuilder.and(predicate)
        }.and(specification)

        return getPaged(tenantSpecification, pageable)
    }

    fun <T> rePaged(content:List<T>,pageable: Pageable):Page<T>{
        val totalSize = content.size
        val start = if(pageable.pageSize * pageable.pageNumber > totalSize){
            totalSize
        } else {
            pageable.pageSize * pageable.pageNumber
        }
        val end = if(pageable.pageSize * (pageable.pageNumber + 1) > totalSize){
            totalSize
        } else {
            pageable.pageSize * (pageable.pageNumber + 1)
        }


        val newContent = content.subList(start,end)
        return PageImpl(newContent,pageable,totalSize.toLong())
    }

}