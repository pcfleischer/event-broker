package com.github.pcfleischer.eventbroker.processors

import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import java.util.function.Consumer
import java.util.function.Function

@Component
class EventListenConsumer : Consumer<Message<String>> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun accept(message: Message<String>) {
        logger.info("Listened to message: $message")
        val payload = message.payload
        logger.info("Listened payload: $payload")
    }
}