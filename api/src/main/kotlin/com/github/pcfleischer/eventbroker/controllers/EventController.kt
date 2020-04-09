package com.github.pcfleischer.eventbroker.controllers

import com.github.pcfleischer.eventbroker.models.getEventId
import com.github.pcfleischer.eventbroker.processors.EventCallbackConsumer

import com.github.pcfleischer.eventbroker.suppliers.EventSupplier
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.Duration

@Component
@RequestMapping("/events")
@RestController
class EventController(
        val eventCallbackConsumer: EventCallbackConsumer,
        val eventSupplier: EventSupplier
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun takeOne(): Flux<String> {
        return eventCallbackConsumer.processor
                .take(1)
                .map { it.payload }
                .timeout(Duration.ofSeconds(10))
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun postOne(@RequestBody body: String): Flux<String> {
        logger.info("Received message: $body")
        val eventId = eventSupplier.send(body).getEventId()
        return eventCallbackConsumer.processor
                .filter { eventId == it.getEventId() }
                .take(1)
                .map { it.payload }
                .timeout(Duration.ofSeconds(5))
    }

}