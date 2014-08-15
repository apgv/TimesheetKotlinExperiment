package me.colombiano.timesheet.config

import com.google.inject.Provides
import com.google.inject.binder.AnnotatedBindingBuilder
import com.google.inject.name.Names
import com.stormpath.sdk.api.ApiKeys
import com.stormpath.sdk.client.Client
import com.stormpath.sdk.client.Clients
import com.stormpath.shiro.realm.ApplicationRealm
import com.stormpath.shiro.realm.DefaultGroupRoleResolver
import com.stormpath.shiro.realm.GroupRoleResolver
import org.apache.shiro.guice.web.ShiroWebModule
import org.apache.shiro.realm.Realm
import org.apache.shiro.session.mgt.SessionManager
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager

import javax.servlet.ServletContext

class SecurityWebModule extends ShiroWebModule {

    SecurityWebModule(final ServletContext servletContext) {
        super(servletContext)
    }

    @Override
    protected void bindSessionManager(final AnnotatedBindingBuilder<SessionManager> bind) {
        bind.to(DefaultWebSessionManager.class).asEagerSingleton()
    }

    @Override
    protected void configureShiroWeb() {
        bindRealm().toInstance(realm())
        bindConstant().annotatedWith(Names.named("shiro.loginUrl")).to("/")
        addFilterChain("/rest/auth/**", ANON)
        addFilterChain("/rest/**", AUTHC)
        addFilterChain("/**", ANON)
    }

    @Provides
    Realm realm() {
        final APPLICATION_ID = System.getenv("STORMPATH_TIMESHEET_APPLICATION_ID")
        final realm = new ApplicationRealm()
        realm.client = client()
        realm.applicationRestUrl = "https://api.stormpath.com/v1/applications/${APPLICATION_ID}"
        realm.groupRoleResolver = groupRoleResolver()
        realm
    }

    @Provides
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

    @Provides
    GroupRoleResolver groupRoleResolver() {
        final groupRoleResolver = new DefaultGroupRoleResolver()
        groupRoleResolver.setModes([DefaultGroupRoleResolver.Mode.NAME] as Set)
        groupRoleResolver
    }
}
