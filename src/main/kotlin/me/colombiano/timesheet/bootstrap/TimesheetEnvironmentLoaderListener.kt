package me.colombiano.timesheet.bootstrap

import javax.servlet.ServletContextEvent
import javax.servlet.annotation.WebListener
import org.apache.shiro.web.env.EnvironmentLoader
import org.apache.shiro.web.env.EnvironmentLoaderListener
import javax.servlet.ServletContext
import org.apache.shiro.web.env.WebEnvironment
import org.apache.shiro.web.env.DefaultWebEnvironment
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.cache.MemoryConstrainedCacheManager
import com.stormpath.shiro.client.ClientFactory
import com.stormpath.shiro.realm.GroupRoleResolver
import com.stormpath.shiro.realm.ApplicationRealm
import java.util.Properties
import com.stormpath.shiro.realm.DefaultGroupRoleResolver
import me.colombiano.timesheet.config.StormpathConfig
import org.apache.shiro.cache.CacheManager
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import me.colombiano.timesheet.config.DatabaseConfig
import me.colombiano.timesheet.config.MysqlConfig
import javax.sql.DataSource
import me.colombiano.timesheet.dbmigration.FlywayMigration
import org.apache.shiro.web.filter.mgt.FilterChainResolver
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver
import org.apache.shiro.web.filter.mgt.DefaultFilter

WebListener
class TimesheetEnvironmentLoaderListener : EnvironmentLoaderListener() {
    private val dataSource: HikariDataSource = hikariDataSource(MysqlConfig())

    override fun contextInitialized(sce: ServletContextEvent?) {
        setupSecurity(sce?.getServletContext())
        migrateDatabase(dataSource)
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        super<EnvironmentLoaderListener>.contextDestroyed(sce)
        dataSource.shutdown()
    }

    private fun setupSecurity(servletContext: ServletContext?) {
        val stormpathConfig = StormpathConfig()
        val stormpathApiKeyProperties = stormpathApiKeyProperties(stormpathConfig)
        val cacheManager = MemoryConstrainedCacheManager()
        val stormpathClient = stormpathClient(cacheManager, stormpathApiKeyProperties)
        val groupRoleResolver = groupRoleResolver()
        val stormpathRealm = applicationRealm(stormpathClient, stormpathConfig, groupRoleResolver)
        val securityManager = securityManager(cacheManager, stormpathRealm)
        val filterChainManager = filterChainManager()
        val filterChainResolver = filterChainResolver(filterChainManager)
        val webEnvironment = webEnvironment(securityManager, servletContext, filterChainResolver)
        servletContext?.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, webEnvironment)
    }

    private fun migrateDatabase(dataSource: DataSource) {
        val flywayMigration = FlywayMigration(dataSource)
        flywayMigration.migrate()
    }

    private fun hikariDataSource(databaseConfig: DatabaseConfig): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.setMaximumPoolSize(databaseConfig.maximumPoolSize())
        hikariConfig.setDataSourceClassName(databaseConfig.dataSourceClassName())
        hikariConfig.addDataSourceProperty("serverName", databaseConfig.serverName())
        hikariConfig.addDataSourceProperty("port", databaseConfig.port())
        hikariConfig.addDataSourceProperty("databaseName", databaseConfig.databaseName())
        hikariConfig.addDataSourceProperty("user", databaseConfig.username())
        hikariConfig.addDataSourceProperty("password", databaseConfig.password())
        return HikariDataSource(hikariConfig)
    }

    private fun webEnvironment(securityManager: DefaultWebSecurityManager, servletContext: ServletContext?,
                               filterChainResolver: FilterChainResolver): WebEnvironment {
        val webEnvironment = DefaultWebEnvironment()
        webEnvironment.setSecurityManager(securityManager)
        webEnvironment.setServletContext(servletContext)
        webEnvironment.setFilterChainResolver(filterChainResolver)
        return webEnvironment
    }

    private fun filterChainManager(): DefaultFilterChainManager {
        val filterChainManager = DefaultFilterChainManager()
        filterChainManager.addFilter(DefaultFilter.authc.name(),
                                     TimesheetFormAuthenticationFilter())
        filterChainManager.addToChain("/rest/auth/**", DefaultFilter.anon.name())
        filterChainManager.addToChain("/rest/**", DefaultFilter.authc.name())
        filterChainManager.addToChain("/**", DefaultFilter.anon.name())
        return filterChainManager
    }

    private fun filterChainResolver(filterChainManager: DefaultFilterChainManager): PathMatchingFilterChainResolver {
        val filterChainResolver = PathMatchingFilterChainResolver()
        filterChainResolver.setFilterChainManager(filterChainManager)
        return filterChainResolver
    }

    private fun securityManager(cacheManager: CacheManager, stormpathRealm: ApplicationRealm): DefaultWebSecurityManager {
        val securityManager = DefaultWebSecurityManager()
        securityManager.setCacheManager(cacheManager)
        securityManager.setRealm(stormpathRealm)
        return securityManager
    }

    private fun stormpathClient(cacheManager: CacheManager, stormpathApiKeyProperties: Properties): ClientFactory {
        val ClientFactory = ClientFactory()
        ClientFactory.setCacheManager(cacheManager)
        ClientFactory.setApiKeyProperties(stormpathApiKeyProperties)
        return ClientFactory
    }

    private fun stormpathApiKeyProperties(stormpathConfig: StormpathConfig): Properties {
        val properties = Properties()
        properties.setProperty("apiKey.id", stormpathConfig.stormpathApikeyId())
        properties.setProperty("apiKey.secret", stormpathConfig.stormpathApikeySecret())
        return properties
    }

    private fun applicationRealm(stormpathClient: ClientFactory, stormpathConfig: StormpathConfig,
                                 groupRoleResolver: GroupRoleResolver): ApplicationRealm {
        val applicationRealm = ApplicationRealm()
        applicationRealm.setClient(stormpathClient.getClientBuilder()?.build())
        applicationRealm.setApplicationRestUrl(stormpathConfig.stormpathApplicationRestUrl())
        applicationRealm.setGroupRoleResolver(groupRoleResolver)
        return applicationRealm
    }

    private fun groupRoleResolver(): GroupRoleResolver {
        val groupRoleResolver = DefaultGroupRoleResolver()
        groupRoleResolver.setModes(hashSetOf(DefaultGroupRoleResolver.Mode.NAME))
        return groupRoleResolver
    }
}