package com.github.pcfleischer.eventbroker.processors

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.pcfleischer.eventbroker.models.Envelope
import com.github.pcfleischer.eventbroker.models.EventEntity
import com.github.pcfleischer.eventbroker.services.EventEntityService
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.util.function.Consumer
import io.debezium.data.Envelope.Operation

@Component
class EventEntityProcessor(
        private val eventEntityService: EventEntityService,
        private val mapper: ObjectMapper
) : Consumer<Message<Envelope>> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun accept(message: Message<Envelope>) {
        val envelope = message.payload
        if (envelope.source?.table != "event_entity") return

        when(Operation.forCode(envelope.op)) {
            Operation.CREATE, Operation.UPDATE -> {
                val eventEntity = mapper.convertValue(envelope.after, EventEntity::class.java) ?:
                        return
                logger.info("Saving to replica: $eventEntity")
                eventEntityService.saveReplicated(eventEntity)
            }
            Operation.DELETE -> {
                val eventEntity = mapper.convertValue(envelope.before, EventEntity::class.java) ?:
                        return
                logger.info("Deleting from replica: $eventEntity")
                eventEntityService.deleteReplicated(eventEntity)
            }
        }
    }
}