package cn.sunline.saas.modules.db

import cn.sunline.saas.modules.enum.FormatType
import com.sun.istack.NotNull
import javax.persistence.*

@Entity
@Table(
    name = "api",
    indexes = [
        Index(name = "api_server_api_method_unique", columnList = "server_id,api,method",unique = true)
    ]
)
class Api (
    @Id
    var id:Long,

    @NotNull
    @Column(name = "server_id", nullable = false, columnDefinition = "varchar(255) not null")
    val serverId: String,

    @NotNull
    @Column(name = "api", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val api:String,

    @NotNull
    @Column(name = "method", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val method: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "format_type", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val formatType: FormatType = FormatType.Json,

    @Column(nullable = false, columnDefinition = "tinyint(1) default true")
    @NotNull
    var enabled: Boolean = true,
)