package com.github.pcfleischer.eventbroker.configuration

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class DataSourceProperties {
    var poolName: String = ""

    @NotNull
    var driverClass: String = ""

    @NotNull
    var user: String = ""

    var password: String = ""

    @NotNull
    var url: String = ""

    @NotNull
    var properties: Map<String, String> = HashMap()

    @NotNull
    var maxWaitForConnection: Int = 0

    @NotNull
    var validationQuery: String = ""

    @Max(1024)
    @Min(1)
    var maxActive: Int = 0

    @Max(1024)
    @Min(1)
    var minIdle: Int = 0

    @Min(1)
    var timeBetweenEvictionRunsMillis: Int = 0

    @Min(1)
    var minEvictableIdleTimeMillis: Long = 0

    var isRemoveAbandoned: Boolean = false

    //In seconds
    var removeAbandonedTimeout: Int = 0

    var validationIntervalMillis: Int = 0

    //For this to work validationQuery must be set to not null string
    var isTestOnBorrow: Boolean = false
}