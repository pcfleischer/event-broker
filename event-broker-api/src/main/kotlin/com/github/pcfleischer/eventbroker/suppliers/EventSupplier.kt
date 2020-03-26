package com.github.pcfleischer.eventbroker.suppliers

import com.github.pcfleischer.eventbroker.models.EVENT_ID
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.util.*
import java.util.function.Supplier

@Component
class EventSupplier : Supplier<Flux<Message<*>>> {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val springCloudDestinationPropertyName = "spring.cloud.stream.sendto.destination"
    private val processor = EmitterProcessor.create<Message<*>>()

    override fun get(): Flux<Message<*>> {
        return processor
    }

    fun send(payload: String): Message<String> {
        logger.info("Sending message: $payload")
        val message = MessageBuilder
                .withPayload(payload)
                .setHeader(EVENT_ID, UUID.randomUUID())
                .build()
        processor.onNext(message)
        return message
    }

    fun <T> createMessage(payload: T, destination: String): Message<*>? {
        val builder: MessageBuilder<T> = MessageBuilder
                .withPayload(payload)
                .setHeader(EVENT_ID, UUID.randomUUID())
        if (destination.isNotBlank()) {
            builder.setHeader(springCloudDestinationPropertyName, destination)
        }
        return builder.build()
    }
}