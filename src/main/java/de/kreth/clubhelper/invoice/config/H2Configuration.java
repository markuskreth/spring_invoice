package de.kreth.clubhelper.invoice.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2Configuration {
    @Bean
    ServletRegistrationBean<WebServlet> h2ServletRegistration() {
	ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<WebServlet>(
		new WebServlet());
	registrationBean.addUrlMappings("/console/*");
	return registrationBean;
    }
}
