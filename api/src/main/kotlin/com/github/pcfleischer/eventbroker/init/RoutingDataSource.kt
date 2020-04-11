package com.github.pcfleischer.eventbroker.init

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class RoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any? {
        return ctx.get()
    }

    companion object {
        private val ctx = ThreadLocal<DataSourceRoute>()
        fun clearReplicaRoute() {
            ctx.remove()
        }

        fun setReplicaRoute() {
            ctx.set(DataSourceRoute.REPLICA)
        }
    }
}
