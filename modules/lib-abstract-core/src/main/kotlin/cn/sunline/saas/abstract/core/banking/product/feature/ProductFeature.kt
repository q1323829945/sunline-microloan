package cn.sunline.saas.abstract.core.banking.product.feature

import cn.sunline.saas.rule.engine.model.Condition
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: ProductFeature
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/4 8:48
 */
abstract class ProductFeature {
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    lateinit var type: ProductFeatureType

}