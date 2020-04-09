package com.github.pcfleischer.eventbroker.processors

import com.github.pcfleischer.eventbroker.init.DataSourceRouting
import com.github.pcfleischer.eventbroker.init.RoutingDataSource
import com.github.pcfleischer.eventbroker.models.Envelope
import com.github.pcfleischer.eventbroker.models.EventEntity
import com.github.pcfleischer.eventbroker.repositories.EventEntityRepository
import io.debezium.serde.DebeziumSerdes
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.EmitterProcessor
import java.util.*
import java.util.function.Consumer

@Component
class EventEntityProcessor(

) : Consumer<Message<Envelope<EventEntity>>>  {
//    @Autowired
//    private lateinit var eventEntityRepository: EventEntityRepository

    private val logger = LoggerFactory.getLogger(javaClass)

    @DataSourceRouting(RoutingDataSource.Route.REPLICA)
    override fun accept(message: Message<Envelope<EventEntity>>) {
        val eventEntity = message.payload.after
        logger.info("Saving to replica: $eventEntity")
//        eventEntityRepository.save(eventEntity)
    }
}