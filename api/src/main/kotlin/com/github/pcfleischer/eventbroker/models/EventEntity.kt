package com.github.pcfleischer.eventbroker.models

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class EventEntity(
        @Id
        val id: String? = null,

        @JsonProperty("details_json")
        val detailsJson: String? = null
)