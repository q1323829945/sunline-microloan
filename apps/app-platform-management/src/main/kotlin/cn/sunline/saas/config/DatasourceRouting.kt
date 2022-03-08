package cn.sunline.saas.config

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.SQLException
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource

@Component
@EnableJpaRepositories(basePackages = ["cn.sunline.saas"], transactionManagerRef = "transactionManager", entityManagerFactoryRef = "entityManager")
@EnableTransactionManagement
class DatasourceRouting (private val config: DatasourceConfig) {
    private lateinit var multiTenantDataSource: AbstractRoutingDataSource

    private val threadLocal = ThreadLocal<String>()

    private val tenantDataSources: MutableMap<String, DataSource> = ConcurrentHashMap()

    private var logger = KotlinLogging.logger {}

    @Bean(name = ["dataSource"])
    @Primary
    fun dataSource(): DataSource? {
        multiTenantDataSource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any? {
                val key = getTenantDomain()
                if (!key.isNullOrEmpty() && !tenantDataSources.containsKey(key))
                    throw ManagementException(ManagementExceptionCode.TENANT_DATA_SOURCE_INVALID, data = key)
                return key
            }
        }

        multiTenantDataSource.setTargetDataSources(tenantDataSources.toMap())
        multiTenantDataSource.setDefaultTargetDataSource(generateDefaultDataSource())
        multiTenantDataSource.afterPropertiesSet()
        return multiTenantDataSource
    }

    @Bean(name = ["entityManager"])
    @DependsOn("dataSource")
    fun entityManagerFactoryBean(
        builder: EntityManagerFactoryBuilder,
        @Autowired @Qualifier("dataSource") dataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean? {
        return builder.
        dataSource(dataSource).
        packages("cn.sunline.saas").build()
    }

    @Bean(name = ["transactionManager"])
    fun transactionManager(
        @Autowired @Qualifier("entityManager") entityManagerFactoryBean: LocalContainerEntityManagerFactoryBean
    ): JpaTransactionManager? {
        return JpaTransactionManager(entityManagerFactoryBean.getObject()!!)
    }

    fun loadTenantDataSourceOnDemand(name: String) {
        if (tenantDataSources.containsKey(name)) return
        logger.info { "Loading and adding tenant datasource [$name] on demand" }
        addNewTenant(name)
    }

    fun getTenantDataSource(name: String): DataSource? {
        return tenantDataSources[name]
    }

    fun clearTenantDomain() {
        threadLocal.set(null)
    }

    fun setTenantDomain(datasourceContext: String) {
        threadLocal.set(datasourceContext)
    }

    fun getTenantDomain(): String? {
        return threadLocal.get()
    }

    fun removeTenant(name: String): Boolean {
        if (!tenantDataSources.containsKey(name)) {
            logger.info { "Tenant data source is not loaded, skipping removal: $name" }
            return false
        }

        logger.info { "Attempt to close tenant data source: $name" }
        val ds = tenantDataSources[name]

        ds?.connection?.run {
            this.close()
            while(!this.isClosed) {
                Thread.sleep(50)
            }

            logger.info { "Tenant data source has been closed: $name" }
        }

        logger.info { "Attempt to remove tenant data source: $name" }
        tenantDataSources.remove(name)
        multiTenantDataSource.setTargetDataSources(tenantDataSources.toMap())
        multiTenantDataSource.afterPropertiesSet();
        logger.info {"Tenant data source has been removed successfully: $name" }

        return true
    }

    fun addNewTenant(name: String): Boolean {
        if (tenantDataSources.containsKey(name)) {
            logger.info { "Tenant datasource already exists, skipping add: $name" }
            return false
        }

        val dataSource = generateTenantDataSource(name)

        return try {
            if (dataSource.connection.isValid(500)) {
                tenantDataSources[name] = dataSource
                multiTenantDataSource.setTargetDataSources(tenantDataSources.toMap())
                multiTenantDataSource.afterPropertiesSet();
                logger.info { "Tenant datasource has been added successfully: $name" }
                true
            } else {
                logger.warn { "Tenant datasource failed to register due to connection issue: $name" }
                false
            }
        } catch (ex: SQLException) {
            logger.warn { "Tenant datasource failed to register: $name - ${ex.localizedMessage}" }
            false
        }
    }

    private fun generateTenantDataSource(tenant: String): DriverManagerDataSource {
        val defaultDataSource = DriverManagerDataSource()
        defaultDataSource.setDriverClassName(config.tenantDatasourceDriver)
        defaultDataSource.url = "${config.tenantDatasourceUrl}/db_tenant_$tenant?${config.tenantDatasourceOptionFlags}"
        defaultDataSource.username = config.tenantDatasourceUsername
        defaultDataSource.password = config.tenantDatasourcePassword
        return defaultDataSource
    }

    private fun generateDefaultDataSource(): DriverManagerDataSource {
        val defaultDataSource = DriverManagerDataSource()
        defaultDataSource.setDriverClassName(config.defaultDatasourceDriver)
        defaultDataSource.url = "${config.defaultDatasourceUrl}/${config.defaultDatasourceDatabase}?${config.defaultDatasourceOptionFlags}"
        defaultDataSource.username = config.defaultDatasourceUsername
        defaultDataSource.password = config.defaultDatasourcePassword
        return defaultDataSource
    }
}