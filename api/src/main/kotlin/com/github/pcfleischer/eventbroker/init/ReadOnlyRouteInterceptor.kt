package com.github.pcfleischer.eventbroker.init


import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Aspect
@Component
@Order(0)
class ReadOnlyRouteInterceptor {
    private val logger = LoggerFactory.getLogger(ReadOnlyRouteInterceptor::class.java)

    @Around("@annotation(transactional)")
    @Throws(Throwable::class)
    fun proceed(proceedingJoinPoint: ProceedingJoinPoint, transactional: Transactional): Any {
        return try {
            if (transactional.readOnly) {
                RoutingDataSource.setReplicaRoute()
                logger.debug("Routing database call to the read replica")
            }
            proceedingJoinPoint.proceed()
        } finally {
            RoutingDataSource.clearReplicaRoute()
        }
    }
}