package cn.sunline.saas.channel.rbac.modules

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
        name = "permission",
        indexes = [
            Index(name = "idx_permission_tag", columnList = "tag"),
            Index(name = "idx_permission_name", columnList = "name"),
        ]
)
class Permission(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
        var tag: String,

        @NotNull
        @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
        var name: String,

        @Column(nullable = true, length = 255, columnDefinition = "varchar(255) not null")
        var remark: String
)