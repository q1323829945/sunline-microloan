package cn.sunline.saas.channel.product.service

import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.channel.product.model.dto.DTOProductAdd
import cn.sunline.saas.channel.product.model.dto.DTOProductChange
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceTest {
    @Autowired
    private lateinit var productService: ProductService

    lateinit var product1Id:String
    lateinit var product2Id:String

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("0")

        val product1 = productService.addOne(
            DTOProductAdd(
                name = "test1",
                productType = ProductType.TEACHER,
                description = null,
                ratePlanId = "1",
            )
        )
        Assertions.assertThat(product1).isNotNull
        Assertions.assertThat(product1.productType).isEqualTo(ProductType.TEACHER)
        product1Id = product1.id

        val product2 = productService.addOne(
            DTOProductAdd(
                name = "test2",
                productType = ProductType.CORPORATE,
                description = null,
                ratePlanId = "1",
            )
        )
        Assertions.assertThat(product2).isNotNull
        Assertions.assertThat(product2.productType).isEqualTo(ProductType.CORPORATE)
        product2Id = product2.id

    }

    @Test
    fun `update one`(){
        val change = productService.updateOne(
            DTOProductChange(
                id = product1Id,
                name = "test1alter",
                productType = ProductType.TEACHER,
                description = null,
                ratePlanId = "222",
            )
        )
        Assertions.assertThat(change.name).isEqualTo("test1alter")
    }

    @Test
    fun `get one`(){
        val product = productService.getProduct(product2Id.toLong())
        Assertions.assertThat(product.name).isEqualTo("test2")
    }

    @Test
    fun `get paged`(){
        val paged = productService.paged(Pageable.unpaged())
        Assertions.assertThat(paged.totalElements).isEqualTo(2)
    }

    @Test
    fun `get one by product type`(){
        val product = productService.getProductByProductType(ProductType.CORPORATE)
        Assertions.assertThat(product).isNotNull
        Assertions.assertThat(product!!.name).isEqualTo("test2")
    }
}