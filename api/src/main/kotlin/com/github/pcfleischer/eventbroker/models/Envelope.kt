package com.github.pcfleischer.eventbroker.models

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class Envelope<T> {
    @JsonProperty("before")
    var before: T? = null

    @JsonProperty("after")
    var after: T? = null

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