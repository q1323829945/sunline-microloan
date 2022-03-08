package cn.sunline.saas.abstract.core.directory

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Directory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 16:43
 */
abstract class Directory(
    @Id
    val id: Long,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var version:String,

    @NotNull
    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1) not null")
    var deleted: Boolean = false,

    @OneToMany
    @JoinColumn(referencedColumnName="directoryId")
    var directoryEntrys:MutableList<DirectoryEntry> = mutableListOf()
)