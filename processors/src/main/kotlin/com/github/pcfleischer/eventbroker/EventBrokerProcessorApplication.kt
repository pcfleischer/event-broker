package com.github.pcfleischer.eventbroker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class EventBrokerProcessorApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(EventBrokerProcessorApplication::class.java)
                    .properties(
                            "spring.config.name:application,core"
                    )
                    .build()
                    .run(*args)
        }
    }
}
