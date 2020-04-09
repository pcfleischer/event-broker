package com.github.pcfleischer.eventbroker.init

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class RoutingDataSource : AbstractRoutingDataSource() {
    enum class Route {
        PRIMARY, REPLICA
    }

    override fun determineCurrentLookupKey(): Any? {
        return ctx.get()
    }

    companion object {
        private val ctx = ThreadLocal<Route>()
        fun clearReplicaRoute() {
            ctx.remove()
        }

        fun setReplicaRoute() {
            ctx.set(Route.REPLICA)
        }
    }
}
