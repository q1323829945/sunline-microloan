package cn.sunline.saas.abstract.core.banking.product

import cn.sunline.saas.abstract.core.banking.product.feature.ConfigurationParameter
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: BankingProduct
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 17:12
 */
abstract class BankingProduct<T:ProductFeature>(
    @Id
    val id: Long? = null,

    @NotNull
    @Column(name = "identification_code", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val identificationCode: String,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val version: String,

    @NotNull
    @Column(nullable = false, length = 512, columnDefinition = "varchar(512) not null")
    var description: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var status: BankingProductStatus = BankingProductStatus.INITIATED,

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "referenceId")
    var configurationOptions: MutableList<ConfigurationParameter> = mutableListOf(),

    @NotNull
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "productId")
    var features: MutableList<T> = mutableListOf()
)

