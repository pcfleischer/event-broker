package com.github.pcfleischer.eventbroker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

/*
    Do not attempt to use @EnableBinding(Source::class) in combination with functional binding, it disables the mapping
 */
@SpringBootApplication
class EventBrokerApiApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(EventBrokerApiApplication::class.java)
                    .properties(
                            "spring.config.name:application,core"
                    )
                    .build()
                    .run(*args)
        }
    }
}
