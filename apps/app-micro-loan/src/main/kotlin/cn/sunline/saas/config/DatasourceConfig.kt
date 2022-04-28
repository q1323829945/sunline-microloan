package cn.sunline.saas.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "datasource")
@Configuration
class DatasourceConfig (
    var defaultDatasourceUrl: String = "jdbc:mysql://localhost:3306",
    var defaultDatasourceUsername: String = "",
    var defaultDatasourcePassword: String = "",
    var defaultDatasourceDatabase: String = "db_saas_loan",
    var defaultDatasourceDriver: String = "com.mysql.cj.jdbc.Driver",
    var defaultDatasourceOptionFlags: String = "useSSL=false&serverTimezone=UTC",
)