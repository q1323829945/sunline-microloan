package cn.sunline.saas.loanuploadconfigure.service

import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureView
import cn.sunline.saas.loan.configure.services.LoanUploadConfigureService
import cn.sunline.saas.loan.product.service.LoanProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class UploadConfigureService {
    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    @Autowired
    private lateinit var loanProductService: LoanProductService

    fun getPaged(pageable: Pageable): Page<DTOUploadConfigureView>{
        val paged = loanUploadConfigureService.getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Boolean>("deleted"),false))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable).map {
            val product = loanProductService.getLoanProduct(it.productId)
            DTOUploadConfigureView(
                id = it.id!!.toString(),
                name = it.name,
                productName = product.name,
                required = it.required
            )
        }

        return paged
    }

}