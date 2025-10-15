package com.farmtastic;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpToHttpsConfig {
	
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
		return server -> {
			Connector connector = new Connector();
			connector.setScheme("http");
			connector.setPort(80);
			connector.setSecure(false);
			connector.setRedirectPort(443);
			
			server.addAdditionalTomcatConnectors(connector);
		};
	}
}
