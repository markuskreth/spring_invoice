package de.kreth.clubhelper.invoice.config;

import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;

@KeycloakConfiguration
@EnableWebSecurity
public class UiSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    private KeycloakClientRequestFactory factory;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
	KeycloakAuthenticationProvider keyCloakAuthProvider = keycloakAuthenticationProvider();
	keyCloakAuthProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
	auth.authenticationProvider(keyCloakAuthProvider);
    }

    @Bean
    public KeycloakConfigResolver keyCloakConfigResolver() {
	return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate restTemplate() {
	return new KeycloakRestTemplate(factory);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	return new RegisterSessionAuthenticationStrategy(
		new SessionRegistryImpl());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
	super.configure(http);

	http.csrf().disable()
		.anonymous().disable()
		.authorizeRequests()
		.requestMatchers(this::isFrameworkInternalRequest).permitAll()
		.antMatchers("/console/**").permitAll()
		.mvcMatchers("", "user")
		.hasAnyRole("ROLE_trainer", "ROLE_admin", "trainer", "admin", "TRAINER", "ADMIN")
		.anyRequest().hasAnyRole("ROLE_trainer", "ROLE_admin", "trainer", "admin", "TRAINER", "ADMIN");
    }

    boolean isFrameworkInternalRequest(HttpServletRequest request) {
	final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);

	return parameterValue != null
		&& Stream.of(HandlerHelper.RequestType.values())
			.anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
	web.ignoring().antMatchers(
		"/VAADIN/**",
		"/favicon.ico",
		"/robots.txt",
		"/manifest.webmanifest",
		"/sw.js",
		"/offline-page.html",
		"/frontend/**",
		"/webjars/**",
		"/frontend-es5/**",
		"/frontend-es6/**");
    }

}
