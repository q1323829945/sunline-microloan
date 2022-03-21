package cn.sunline.saas.abstract.core.banking.product

import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import cn.sunline.saas.rule.engine.model.Condition
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: BankingProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 17:12
 */
abstract class BankingProduct{

    @NotNull
    @Column(name = "identification_code", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    lateinit var identificationCode: String

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    lateinit var name: String

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    lateinit var version: String

    @NotNull
    @Column(nullable = false, length = 512, columnDefinition = "varchar(512) not null")
    lateinit var description: String

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var status: BankingProductStatus = BankingProductStatus.INITIATED

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "referenceId")
    var configurationOptions: MutableList<Condition> = mutableListOf()

}

