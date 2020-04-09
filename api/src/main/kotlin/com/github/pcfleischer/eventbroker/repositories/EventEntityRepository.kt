package com.github.pcfleischer.eventbroker.repositories

import com.github.pcfleischer.eventbroker.models.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventEntityRepository : JpaRepository<EventEntity, Long>