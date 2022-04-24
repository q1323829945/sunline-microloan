package cn.sunline.saas.loanuploadconfigure.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureAdd
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureView
import cn.sunline.saas.loan.configure.services.LoanUploadConfigureService
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.loanuploadconfigure.exception.ConfigureNotFoundException
import cn.sunline.saas.loanuploadconfigure.exception.LoanUploadConfigureBusinessException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getPaged(pageable: Pageable): Page<DTOUploadConfigureView>{
        val paged = loanUploadConfigureService.getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Boolean>("deleted"),false))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable)
        return paged.map { objectMapper.convertValue<DTOUploadConfigureView>(it) }
    }

    fun getAll(): Page<DTOUploadConfigureView>{
        val paged = loanUploadConfigureService.getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Boolean>("deleted"),false))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable = Pageable.unpaged())
        return paged.map { objectMapper.convertValue<DTOUploadConfigureView>(it) }
    }


    fun addUploadConfigure(dtoUploadConfigureAdd: DTOUploadConfigureAdd): DTOUploadConfigureView {
        val uploadConfigure = objectMapper.convertValue<LoanUploadConfigure>(dtoUploadConfigureAdd)
        val save = loanUploadConfigureService.addOne(uploadConfigure)
        return objectMapper.convertValue(save)
    }


    fun deleteUploadConfigure(id: Long): DTOUploadConfigureView {
        val mapping = loanProductService.getLoanProductLoanUploadConfigureMapping(id)
        if(mapping>0){
            throw LoanUploadConfigureBusinessException("The config is using,no-supported deleted",ManagementExceptionCode.LOAN_UPLOAD_CONFIGURE_NOT_FOUND)
        }
        val uploadConfigure = loanUploadConfigureService.getOne(id) ?: throw ConfigureNotFoundException("Invalid configure")
        uploadConfigure.deleted = true
        val save = loanUploadConfigureService.save(uploadConfigure)
        return objectMapper.convertValue(save)
    }

}