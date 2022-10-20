package cn.sunline.saas.modules.db

import com.sun.istack.NotNull
import javax.persistence.*

@Entity
@Table(
    name = "server",
    indexes = [
        Index(name = "instance_server_unique", columnList = "instance_id,server",unique = true)
    ]
)
class Server(
    @Id
    var id:Long,

    @NotNull
    @Column(name = "instance_id", nullable = false, columnDefinition = "varchar(255) not null")
    val instanceId: String,

    @NotNull
    @Column(name = "server", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val server:String,

    @NotNull
    @Column(name = "domain", nullable = false, columnDefinition = "varchar(1024) not null")
    var domain: String,

    @Column(nullable = false, columnDefinition = "tinyint(1) default true")
    @NotNull
    var enabled: Boolean = true,
){
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH], orphanRemoval = true, mappedBy = "serverId")
    var apis: MutableList<Api> = mutableListOf()
        set(value) {
            this.apis.clear()
            field.addAll(value)
        }

}