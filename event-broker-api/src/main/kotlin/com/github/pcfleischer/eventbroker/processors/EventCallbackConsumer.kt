package com.github.pcfleischer.eventbroker.processors

import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import reactor.core.publisher.EmitterProcessor
import java.util.function.Consumer


@Component
class EventCallbackConsumer : Consumer<Message<String>> {
    val processor : EmitterProcessor<Message<String>> = EmitterProcessor.create(false)
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun accept(message: Message<String>) {
        logger.info("Callback message: $message")
        val payload = message.payload
        logger.info("Callback payload: $payload")
        processor.onNext(message)
    }
}