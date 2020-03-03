package cz.tripsis.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;
import oxus.lib.common.spring.TraceResponseFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableSwagger2WebFlux
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		return  route(GET("/swagger/index.html"), req ->
				ServerResponse.temporaryRedirect(URI.create("/swagger-ui.html"))
							  .build());
	}

	@Bean
	WebFilter traceResponseFilter() {
		return new TraceResponseFilter();
	}

}
