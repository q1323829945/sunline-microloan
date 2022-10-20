package cn.sunline.saas.modules.db

import com.sun.istack.NotNull
import javax.persistence.*

@Entity
@Table(
    name = "instance",
    indexes = [
        Index(name = "instance_tenant_unique", columnList = "tenant",unique = true)
    ]
)
class Instance (
    @Id
    val id:String,

    @NotNull
    @Column(name = "tenant", nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    val tenant:String,

    @NotNull
    @Column(name = "access_key", nullable = false, columnDefinition = "text not null")
    var accessKey: String,

    @NotNull
    @Column(name = "secret_key", nullable = false, columnDefinition = "text not null")
    var secretKey: String,

    @Column(nullable = false, columnDefinition = "tinyint(1) default true")
    @NotNull
    var enabled: Boolean = true,
){
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH], orphanRemoval = true, mappedBy = "instanceId")
    var server: MutableList<Server> = mutableListOf()
        set(value) {
            this.server.clear()
            field.addAll(value)
        }
}