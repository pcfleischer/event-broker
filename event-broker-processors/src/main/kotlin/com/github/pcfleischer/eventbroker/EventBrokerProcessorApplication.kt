package com.github.pcfleischer.eventbroker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
//@ComponentScan(basePackages = {"com.github.pcfleischer.eventbroker.processors"})
//@Import(EventProcessor)
class EventBrokerProcessorApplication

fun main(args: Array<String>) {
	runApplication<EventBrokerProcessorApplication>(*args)
}
