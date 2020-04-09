package com.github.pcfleischer.eventbroker.core

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DataSourceFactory {
    fun create(configuration: DataSourceProperties): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.poolName = configuration.poolName
        hikariConfig.jdbcUrl = configuration.url
        hikariConfig.username = configuration.user
        hikariConfig.password = configuration.password
        hikariConfig.driverClassName = configuration.driverClass
        return HikariDataSource(hikariConfig)
    }
}