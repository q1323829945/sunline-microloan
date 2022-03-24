package cn.sunline.saas.loan.configure.modules.db

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "loan_upload_config",
        indexes = [
            Index(name = "idx_deleted", columnList = "deleted")
        ])
class LoanUploadConfigure (
        @Id
        var id: Long? = null,

        @NotNull
        @Column(name = "name", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
        var name:String,

        @NotNull
        @Column( nullable = false, columnDefinition = "tinyint(1) not null")
        var required: Boolean,

        @Column(nullable = false, columnDefinition = "tinyint(1) default false")
        @NotNull
        var deleted: Boolean = false,

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var created: Date? = null,

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var updated: Date? = null
)