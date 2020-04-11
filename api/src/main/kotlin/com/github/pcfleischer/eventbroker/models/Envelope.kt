package com.github.pcfleischer.eventbroker.models

import com.fasterxml.jackson.annotation.*
import io.debezium.data.Envelope

@JsonInclude(JsonInclude.Include.NON_NULL)
class Envelope {
    @JsonProperty("before")
    var before: Any? = null

    @JsonProperty("after")
    var after: Any? = null

    @JsonProperty("source")
    var source: Source? = null

    @JsonProperty("op")
    var op: String? = null

    @JsonProperty("ts_ms")
    var tsMs: Long? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String?, Any?> = HashMap()

    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String?, Any?> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String?, value: Any?) {
        additionalProperties[name] = value
    }
}