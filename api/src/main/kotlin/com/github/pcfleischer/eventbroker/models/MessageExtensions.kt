package com.github.pcfleischer.eventbroker.models

import org.springframework.messaging.Message

const val EVENT_ID = "event_id"

fun Message<*>.getEventId(): String {
    return this.headers[EVENT_ID].toString()
}

fun Message<*>.setEventId(eventId: String) {
    this.headers[EVENT_ID] = eventId
}