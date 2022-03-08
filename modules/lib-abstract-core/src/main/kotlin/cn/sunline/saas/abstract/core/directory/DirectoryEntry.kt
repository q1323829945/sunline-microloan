package cn.sunline.saas.abstract.core.directory

import javax.persistence.Column
import javax.persistence.Id
import javax.validation.constraints.NotNull

/**
 * @title: DirectoryEntry
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/4 14:02
 */
abstract class DirectoryEntry(
    @Id
    val id: Long? = null,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    val key: String,

    @NotNull
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var value: String,

    @NotNull
    @Column(nullable = false, columnDefinition = "bigint not null")
    val directoryId: Long

)