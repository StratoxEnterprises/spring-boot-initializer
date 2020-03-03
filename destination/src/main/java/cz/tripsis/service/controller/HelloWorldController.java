package cz.tripsis.service.controller;

import cz.tripsis.service.model.HelloWorldResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {
    Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    @GetMapping
    private Mono<HelloWorldResponse> sayHello() {
        log.info("Hello World Controller");
        return Mono.just(new HelloWorldResponse("Hello World"));
    }
}
