package cn.sunline.saas.pdpa.modules.db

import cn.sunline.saas.global.constant.CountryType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "pdpa",
        indexes = [
            Index(name = "idx_customer_id_unique", columnList = "customer_id",unique = true)
        ]
)
class PDPA (
        @Id
        var id: Long? = null,

        @NotNull
        @Column(name = "customer_id", nullable = false, columnDefinition = "bigint not null")
        var customerId:Long,

        @NotNull
        @Column(name = "country_type", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
        var countryType:CountryType,

        @NotNull
        @Column(name = "company_data", nullable = false,  columnDefinition = "text not null")
        var companyData:String,

        @NotNull
        @Column(name = "personal_data", nullable = false, columnDefinition = "tinyint(1) not null")
        var personalData:String,

        @Column(name = "signature", nullable = false, length = 256, columnDefinition = "varchar(256) ")
        var signature:String? = null,

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var created: Date? = null,

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var updated: Date? = null
)