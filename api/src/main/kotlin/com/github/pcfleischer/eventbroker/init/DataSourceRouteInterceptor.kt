package com.github.pcfleischer.eventbroker.init

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(0)
class DataSourceRouteInterceptor {
    private val logger = LoggerFactory.getLogger(DataSourceRouteInterceptor::class.java)

    @Around("@annotation(dataSourceRouting)")
    @Throws(Throwable::class)
    fun proceed(proceedingJoinPoint: ProceedingJoinPoint, dataSourceRouting: DataSourceRouting): Any {
        return try {
            if (dataSourceRouting.route == DataSourceRoute.REPLICA) {
                RoutingDataSource.setReplicaRoute()
                logger.debug("Routing database call to the read replica")
            }
            proceedingJoinPoint.proceed()
        } finally {
            RoutingDataSource.clearReplicaRoute()
        }
    }
}