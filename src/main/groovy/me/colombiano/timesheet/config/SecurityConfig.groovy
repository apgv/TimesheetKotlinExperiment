package me.colombiano.timesheet.config

import com.stormpath.sdk.api.ApiKeys
import com.stormpath.sdk.client.Client
import com.stormpath.sdk.client.Clients
import com.stormpath.shiro.realm.ApplicationRealm
import com.stormpath.shiro.realm.DefaultGroupRoleResolver
import com.stormpath.shiro.realm.GroupRoleResolver
import org.apache.shiro.cache.CacheManager
import org.apache.shiro.cache.MemoryConstrainedCacheManager
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.realm.Realm
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.filter.authc.AnonymousFilter
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import static me.colombiano.timesheet.Urls.LOGIN

@Configuration
class SecurityConfig {

    @Bean(name = "shiroFilter")
    ShiroFilterFactoryBean shiroFilterFactoryBean() {
        final shiroFilterFactoryBean = new ShiroFilterFactoryBean()
        shiroFilterFactoryBean.securityManager = securityManager()
        shiroFilterFactoryBean.loginUrl = LOGIN.url
        shiroFilterFactoryBean.unauthorizedUrl = LOGIN.url
        shiroFilterFactoryBean.filters = [anon : anonymousFilter(),
                                          authc: passThruAuthenticationFilter()]
        shiroFilterFactoryBean.filterChainDefinitionMap = ["/public/secured/**": "authc",
                                                           "/public/**"        : "anon",
                                                           "/auth/**"          : "anon"]
        shiroFilterFactoryBean
    }

    @Bean(name = "anon")
    AnonymousFilter anonymousFilter() {
        new AnonymousFilter()
    }

    @Bean(name = "authc")
    PassThruAuthenticationFilter passThruAuthenticationFilter() {
        final filter = new PassThruAuthenticationFilter()
        filter.loginUrl = LOGIN.url
        filter
    }

    @Bean
    SecurityManager securityManager() {
        def securityManager = new DefaultWebSecurityManager()
        securityManager.cacheManager = cacheManager()
        securityManager.realm = realm()
        securityManager
    }

    @Bean
    LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        new LifecycleBeanPostProcessor()
    }

    @Bean
    CacheManager cacheManager() {
        new MemoryConstrainedCacheManager()
    }

    @Bean
    Realm realm() {
        final APPLICATION_ID = System.getenv("STORMPATH_TIMESHEET_APPLICATION_ID")
        final realm = new ApplicationRealm()
        realm.client = client()
        realm.applicationRestUrl = "https://api.stormpath.com/v1/applications/${APPLICATION_ID}"
        realm.groupRoleResolver = groupRoleResolver()
        realm
    }

    @Bean
    GroupRoleResolver groupRoleResolver() {
        final groupRoleResolver = new DefaultGroupRoleResolver()
        groupRoleResolver.setModes([DefaultGroupRoleResolver.Mode.NAME] as Set)
        groupRoleResolver
    }

    @Bean
    Client client() {
        final properties = new Properties()
        properties.putAll(["apiKey.id"    : System.getenv("STORMPATH_APIKEY_ID"),
                           "apiKey.secret": System.getenv("STORMPATH_APIKEY_SECRET")])

        final apiKey = ApiKeys.builder()
                .setProperties(properties)
                .build()

        Clients.builder()
                .setApiKey(apiKey)
                .build()
    }
}
