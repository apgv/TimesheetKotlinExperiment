package me.colombiano.timesheet.security

import org.apache.shiro.web.env.DefaultWebEnvironment
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.filter.mgt.FilterChainResolver
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver
import org.apache.shiro.cache.CacheManager
import com.stormpath.shiro.realm.ApplicationRealm
import java.util.Properties
import com.stormpath.shiro.client.ClientFactory
import me.colombiano.timesheet.config
import com.stormpath.shiro.realm.GroupRoleResolver
import org.apache.shiro.cache.MemoryConstrainedCacheManager
import com.stormpath.shiro.realm.DefaultGroupRoleResolver
import org.apache.shiro.web.filter.mgt.DefaultFilter
import javax.servlet.ServletContext
import org.apache.shiro.web.env.EnvironmentLoader
import org.apache.shiro.web.env.WebEnvironment

trait TimesheetShiroSecurity {

    fun secureWebApplication(servletContext: ServletContext?) {
        val stormpathConfig = StormpathConfig()
        val stormpathApiKeyProperties = stormpathApiKeyProperties(stormpathConfig)
        val cacheManager = MemoryConstrainedCacheManager()
        val stormpathClient = stormpathClient(cacheManager, stormpathApiKeyProperties)
        val groupRoleResolver = groupRoleResolver()
        val stormpathRealm = applicationRealm(stormpathClient, stormpathConfig, groupRoleResolver)
        val securityManager = securityManager(cacheManager, stormpathRealm)
        val filterChainManager = filterChainManager()
        val filterChainResolver = filterChainResolver(filterChainManager)
        val webEnvironment = webEnvironment(securityManager, filterChainResolver, servletContext)
        servletContext?.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, webEnvironment)
    }

    private fun webEnvironment(securityManager: DefaultWebSecurityManager,
                               filterChainResolver: FilterChainResolver,
                               servletContext: ServletContext?): WebEnvironment {
        val webEnvironment = DefaultWebEnvironment()
        webEnvironment.setSecurityManager(securityManager)
        webEnvironment.setFilterChainResolver(filterChainResolver)
        webEnvironment.setServletContext(servletContext)
        return webEnvironment
    }

    private fun filterChainManager(): DefaultFilterChainManager {
        val filterChainManager = DefaultFilterChainManager()
        filterChainManager.addFilter(DefaultFilter.authc.name(), TimesheetFormAuthenticationFilter())
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

    private fun securityManager(cacheManager: CacheManager, realm: ApplicationRealm): DefaultWebSecurityManager {
        val securityManager = DefaultWebSecurityManager()
        securityManager.setCacheManager(cacheManager)
        securityManager.setRealm(realm)
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

    private fun applicationRealm(stormpathClient: ClientFactory,
                                 stormpathConfig: StormpathConfig,
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