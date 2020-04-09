package com.github.pcfleischer.eventbroker.processors

import com.github.pcfleischer.eventbroker.models.Envelope
import com.github.pcfleischer.eventbroker.models.EventEntity
import io.debezium.serde.DebeziumSerdes
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.EmitterProcessor
import java.util.*
import java.util.function.Consumer


@Component
class EventEntityListenConsumer : Consumer<Message<Envelope<EventEntity>>> {
    val processor: EmitterProcessor<String> = EmitterProcessor.create(false)
    private val logger = LoggerFactory.getLogger(javaClass)

    /**Serde to be used in Kafka Streams operations. This is a custom Serde that uses the same
     * Avro message converter used by the inbound serialization by the framework. This Serde implementation
     * will interact with the Spring Cloud Stream provided Schema Registry for resolving schema.  */
//    @Autowired
//    @Qualifier("messageConverterDelegateSerde")
//    private val customSerde: MessageConverterDelegateSerde<Envelope>? = null

    override fun accept(message: Message<Envelope<EventEntity>>) {
        val serdes = DebeziumSerdes.payloadJson(HashMap::class.java)
        val configs: Map<String, String> = mutableMapOf(
              //  "from.field" to "after",
                "unknown.properties.ignored" to "true"
        )
        serdes.configure(configs, false)
        //val eventEntity = serdes.deserializer().deserialize("events.public.event_entity", message.payload)
        logger.info("Listen message: $message")
//        val payload = message.payload
//        logger.info("Listen payload: $payload")
        processor.onNext(message.toString())
    }
}