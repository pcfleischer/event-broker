package com.github.pcfleischer.eventbroker.processors

import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class EventProcessor : Function<Message<String>, Message<String>> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun apply(message: Message<String>): Message<String> {
        logger.info("Processing message: $message")
        val payload = message.payload.toUpperCase()
        logger.info("Processed message: $payload")
        return MessageBuilder.withPayload(payload).build()
    }
}