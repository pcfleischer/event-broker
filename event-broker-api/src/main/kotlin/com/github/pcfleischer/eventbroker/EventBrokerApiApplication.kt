package com.github.pcfleischer.eventbroker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.function.context.catalog.BeanFactoryAwareFunctionRegistry

/*
    Do not attempt to use @EnableBinding(Source::class) in combination with functional binding, it disables the mapping
 */
@SpringBootApplication
class EventBrokerApiApplication {

}

fun main(args: Array<String>) {
    SpringApplication.run(EventBrokerApiApplication::class.java, *args)
}
