package com.github.pcfleischer.eventbroker.services

import com.github.pcfleischer.eventbroker.init.DataSourceRoute
import com.github.pcfleischer.eventbroker.init.DataSourceRouting
import com.github.pcfleischer.eventbroker.models.EventEntity
import com.github.pcfleischer.eventbroker.repositories.EventEntityRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventEntityService(
        private val eventEntityRepository: EventEntityRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @DataSourceRouting(DataSourceRoute.REPLICA)
    fun saveReplicated(eventEntity: EventEntity) {
        logger.info("Saving to replica: $eventEntity")
        eventEntityRepository.save(eventEntity)
    }

    @DataSourceRouting(DataSourceRoute.REPLICA)
    fun deleteReplicated(eventEntity: EventEntity) {
        logger.info("Deleting from replica: $eventEntity")
        eventEntityRepository.delete(eventEntity)
    }
}