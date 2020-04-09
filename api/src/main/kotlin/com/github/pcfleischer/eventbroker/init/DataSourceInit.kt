package com.github.pcfleischer.eventbroker.init

import com.github.pcfleischer.eventbroker.configuration.ApiProperties
import com.github.pcfleischer.eventbroker.core.DataSourceFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataSourceInit(
        private val properties: ApiProperties,
        private val dataSourceFactory: DataSourceFactory
) {
    @Bean
    @Primary
    fun dataSource(): DataSource {
        val primaryDataSource = dataSourceFactory.create(properties.primaryDataSource)
        val replicaDataSource = dataSourceFactory.create(properties.replicaDataSource)
        val targetDataSources: MutableMap<Any, Any> = mutableMapOf(
                RoutingDataSource.Route.PRIMARY to primaryDataSource,
                RoutingDataSource.Route.REPLICA to replicaDataSource
        )

        val routingDataSource = RoutingDataSource()
        routingDataSource.setTargetDataSources(targetDataSources)
        routingDataSource.setDefaultTargetDataSource(primaryDataSource)
        return routingDataSource
    }
}