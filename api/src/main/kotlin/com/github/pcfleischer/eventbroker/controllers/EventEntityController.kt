package com.github.pcfleischer.eventbroker.controllers

import com.github.pcfleischer.eventbroker.init.DataSourceRouting
import com.github.pcfleischer.eventbroker.init.RoutingDataSource
import com.github.pcfleischer.eventbroker.models.EventEntity
import com.github.pcfleischer.eventbroker.processors.EventEntityListenConsumer
import com.github.pcfleischer.eventbroker.repositories.EventEntityRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*

@Component
@RequestMapping("/data-events")
@RestController
class EventEntityController(
        val eventEntityRepository: EventEntityRepository,
        val eventEntityListenConsumer: EventEntityListenConsumer
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    @Transactional(readOnly = true)
    fun findAll(): List<EventEntity> {
        return eventEntityRepository.findAll()
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    //@DataSourceRouting(RoutingDataSource.Route.REPLICA)
    fun postOne(@RequestBody body: String): Flux<String> {
        logger.info("Received message: $body")
        val event = EventEntity(
                id = UUID.randomUUID().toString(),
                detailsJson = body
        )
        val savedEvent = eventEntityRepository.save(event)
        return eventEntityListenConsumer.processor
                .take(1)
                .timeout(Duration.ofSeconds(5))
    }

}