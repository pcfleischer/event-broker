package com.github.pcfleischer.eventbroker.models

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("version", "name", "db", "ts_usec", "txId", "lsn", "schema", "table", "snapshot")
class Source {
    @JsonProperty("version")
    var version: String? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("db")
    var db: String? = null

    @JsonProperty("ts_usec")
    var tsUsec: Long? = null

    @JsonProperty("txId")
    var txId: Long? = null

    @JsonProperty("lsn")
    var lsn: Long? = null

    @JsonProperty("schema")
    var schema: String? = null

    @JsonProperty("table")
    var table: String? = null

    @JsonProperty("snapshot")
    var snapshot: Boolean? = null

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