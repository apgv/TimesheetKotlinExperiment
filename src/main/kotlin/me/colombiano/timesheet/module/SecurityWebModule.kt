package me.colombiano.timesheet.module

import org.apache.shiro.guice.web.ShiroWebModule
import org.apache.shiro.guice.web.ShiroWebModule.*
import javax.servlet.ServletContext
import com.stormpath.sdk.client.Clients
import com.stormpath.shiro.realm.GroupRoleResolver
import com.google.inject.Provides
import com.stormpath.shiro.realm.DefaultGroupRoleResolver
import org.apache.shiro.realm.Realm
import com.google.inject.name.Names
import com.stormpath.shiro.realm.ApplicationRealm
import com.stormpath.sdk.client.Client
import me.colombiano.timesheet.config.SecurityConfig
import java.util.Properties
import com.stormpath.sdk.api.ApiKeys

class SecurityWebModule(servletContext: ServletContext?) : ShiroWebModule(servletContext) {

    override fun configureShiroWeb() {
        bindRealm()!!.to(javaClass<Realm>())
        bindConstant()!!.annotatedWith(Names.named("shiro.loginUrl"))!!.to("/")
        addFilterChain("/rest/auth/**", ANON)
        addFilterChain("/rest/**", AUTHC)
        addFilterChain("/**", ANON)
    }

    Provides
    fun realm(client: Client, securityConfig: SecurityConfig, groupRoleResolver: GroupRoleResolver): Realm {
        val realm = ApplicationRealm()
        realm.setClient(client)
        realm.setApplicationRestUrl(securityConfig.stormpathApplicationRestUrl())
        realm.setGroupRoleResolver(groupRoleResolver)
        return realm
    }

    Provides
    fun client(securityConfig: SecurityConfig): Client? {
        val properties = Properties()
        properties.setProperty("apiKey.id", securityConfig.stormpathApikeyId())
        properties.setProperty("apiKey.secret", securityConfig.stormpathApikeySecret())

        val apiKey = ApiKeys.builder()
                ?.setProperties(properties)
                ?.build()

        return Clients.builder()
                ?.setApiKey(apiKey)
                ?.build()
    }

    Provides
    fun groupRoleResolver(): GroupRoleResolver {
        val groupRoleResolver = DefaultGroupRoleResolver()
        groupRoleResolver.setModes(hashSetOf(DefaultGroupRoleResolver.Mode.NAME))
        return groupRoleResolver
    }
}