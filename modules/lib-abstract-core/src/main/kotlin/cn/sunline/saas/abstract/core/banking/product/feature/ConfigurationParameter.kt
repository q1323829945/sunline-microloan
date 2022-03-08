package cn.sunline.saas.abstract.core.banking.product.feature

import javax.persistence.Column
import javax.validation.constraints.NotNull

/**
 * @title: ConfigurationParameter
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/4 8:49
 */
abstract class ConfigurationParameter(

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val type: String,

    @NotNull
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var description: String,

    @NotNull
    @Column(name="value_range",nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    var valueRange: String,

    @NotNull
    @Column(name="reference_id",nullable = false,  columnDefinition = "bigint not null")
    val referenceId:Long
)