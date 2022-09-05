package cn.sunline.saas.channel.product.service

import cn.sunline.saas.channel.product.exception.LoanProductNotFoundException
import cn.sunline.saas.channel.product.exception.ProductTypeAlreadyExistException
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.channel.product.model.db.Product
import cn.sunline.saas.channel.product.model.db.Questionnaire
import cn.sunline.saas.channel.product.model.dto.DTOProductAdd
import cn.sunline.saas.channel.product.model.dto.DTOProductChange
import cn.sunline.saas.channel.product.model.dto.DTOProductView
import cn.sunline.saas.channel.product.repository.ProductRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class ProductService(
    private var productRepos: ProductRepository,
    private var sequence: Sequence,
) : BaseMultiTenantRepoService<Product, Long>(productRepos) {

    @Autowired
    private lateinit var questionnaireService: QuestionnaireService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun addOne(dtoProductAdd: DTOProductAdd): DTOProductView {
        val checkProductType = getProductByProductType(dtoProductAdd.productType)

        checkProductType?.run {
            throw ProductTypeAlreadyExistException("product type already exist")
        }

        val productId = sequence.nextId()

        val questionnaires = mutableListOf<Questionnaire>()
        dtoProductAdd.questionnaires?.forEach {
            val questionnaire = questionnaireService.getOne(it.toLong())
            questionnaire?.run {
                questionnaires.add(this)
            }
        }


        val product = save(
            Product(
                id = productId,
                name = dtoProductAdd.name,
                productType = dtoProductAdd.productType,
                description = dtoProductAdd.description,
                ratePlanId = dtoProductAdd.ratePlanId.toLong(),
                questionnaires = questionnaires
            )
        )

        return DTOProductView(
            id = product.id.toString(),
            name = product.name,
            productType = product.productType,
            description = product.description,
            ratePlanId = product.ratePlanId.toString(),
            questionnaires = objectMapper.convertValue(product.questionnaires)
        )
    }

    fun updateOne(dtoProductChange: DTOProductChange): DTOProductView {
        val oldOne = this.getOne(dtoProductChange.id.toLong()) ?: throw LoanProductNotFoundException("Invalid product")


        val questionnaires = mutableListOf<Questionnaire>()
        dtoProductChange.questionnaires?.forEach {
            val questionnaire = questionnaireService.getOne(it.toLong())
            questionnaire?.run {
                questionnaires.add(this)
            }
        }
        oldOne.name = dtoProductChange.name
        oldOne.description = dtoProductChange.description
        oldOne.questionnaires = questionnaires
        oldOne.ratePlanId = dtoProductChange.ratePlanId.toLong()

        val product = save(oldOne)

        return DTOProductView(
            id = product.id.toString(),
            name = product.name,
            productType = product.productType,
            description = product.description,
            ratePlanId = product.ratePlanId.toString(),
            questionnaires = objectMapper.convertValue(product.questionnaires)
        )
    }

    fun getProduct(id: Long): DTOProductView {
        val product = this.getOne(id) ?: throw LoanProductNotFoundException("Invalid product")
        return DTOProductView(
            id = product.id.toString(),
            name = product.name,
            productType = product.productType,
            description = product.description,
            ratePlanId = product.ratePlanId.toString(),
            questionnaires = objectMapper.convertValue(product.questionnaires)
        )
    }

    fun getProductByProductType(productType: ProductType): DTOProductView? {
        val product = getOneWithTenant { root, _, criteriaBuilder ->
            val predicate = criteriaBuilder.equal(root.get<ProductType>("productType"), productType)
            criteriaBuilder.and(predicate)
        }

        return product?.run {
            DTOProductView(
                id = product.id.toString(),
                name = product.name,
                productType = product.productType,
                description = product.description,
                ratePlanId = product.ratePlanId.toString(),
                questionnaires = objectMapper.convertValue(product.questionnaires)
            )
        }
    }

    fun paged(pageable: Pageable): Page<DTOProductView> {
        val paged = getPageWithTenant(null, pageable)

        return paged.map { objectMapper.convertValue(it) }
    }
}