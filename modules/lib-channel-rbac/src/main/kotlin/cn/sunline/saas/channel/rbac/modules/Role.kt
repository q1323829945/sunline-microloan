package cn.sunline.saas.channel.rbac.modules

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
        name = "role",
        indexes = [
            Index(name = "idx_role_name", columnList = "name"),
            Index(name = "idx_role_created", columnList = "created"),
            Index(name = "idx_role_updated", columnList = "updated")
        ]
)
class Role (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
        var name: String,

        @Column(nullable = true, length = 255, columnDefinition = "varchar(255) not null")
        var remark: String,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "role_permission_mapping",
                joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "permission_id", referencedColumnName = "id")]
        )
        var permissions: MutableList<Permission> = mutableListOf(),

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var created: Date? = null,

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var updated: Date? = null
)