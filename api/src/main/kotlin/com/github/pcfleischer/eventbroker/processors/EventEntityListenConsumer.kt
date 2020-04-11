package com.github.pcfleischer.eventbroker.processors

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.pcfleischer.eventbroker.models.Envelope
import com.github.pcfleischer.eventbroker.models.EventEntity
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.EmitterProcessor
import java.util.function.Consumer

@Component
class EventEntityListenConsumer(
        private val mapper: ObjectMapper
) : Consumer<Message<Envelope>> {
    val processor: EmitterProcessor<String> = EmitterProcessor.create(false)
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun accept(message: Message<Envelope>) {
        logger.info("Received change data capture message: $message")
        val envelope = message.payload
        if (envelope.source?.table != "event_entity") return
        val eventEntity = mapper.convertValue(envelope.after, EventEntity::class.java) ?: return
        processor.onNext(eventEntity.detailsJson)
    }
}