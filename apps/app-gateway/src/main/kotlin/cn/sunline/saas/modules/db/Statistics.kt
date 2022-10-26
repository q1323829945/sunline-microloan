package cn.sunline.saas.modules.db

import com.sun.istack.NotNull
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "statistics",
    indexes = [
        Index(name = "statistics_date_unique", columnList = "datetime",unique = true)
    ]
)
class Statistics(
    @Id
    var id:Long,

    @NotNull
    @Column(name = "tenant", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val tenant:String,

    @NotNull
    @Column(name = "server", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val server:String,

    @NotNull
    @Column(name = "method", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val method:String,

    @NotNull
    @Column(name = "path", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    val path:String,

    @Column(name = "query", nullable = true, length = 256, columnDefinition = "varchar(256) default null")
    val query:String? = null,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var datetime: Date,


 )