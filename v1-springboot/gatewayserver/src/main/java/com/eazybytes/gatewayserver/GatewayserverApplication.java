package com.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}


	//RouteLocator is used to map the custom url with the actual application url
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(
						p -> p
								.path("/eazybank/accounts/**") //this is the custom path
								.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
											   .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								) //here we are mapping that custom path with the all the othe api endpoint of application
								.uri("lb://ACCOUNTS") //here we are declaring the application registered on EUREKA with load balancing
				)
				.route(
						p -> p
								.path("/eazybank/cards/**")
								.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
											   .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								) //segment is just the variable name given for reference within this method.
								.uri("lb://CARDS")
				)
				.route(
						p -> p
								.path("/eazybank/loans/**")
								.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
										       .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								)
								.uri("lb://LOANS")
				).build();
	}

}
