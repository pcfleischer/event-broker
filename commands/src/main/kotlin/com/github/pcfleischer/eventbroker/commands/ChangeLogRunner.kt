package com.github.pcfleischer.eventbroker.commands

import com.github.pcfleischer.eventbroker.core.DataSourceFactory
import liquibase.integration.spring.SpringLiquibase
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(
        scanBasePackages = ["com.github.pcfleischer.eventbroker"],
        exclude = [DataSourceAutoConfiguration::class])
open class ChangeLogRunner(
        private val args: ApplicationArguments?,
        private val properties: CommandProperties,
        private val dataSourceFactory: DataSourceFactory
) : ApplicationRunner {
    companion object {
        private val logger = LoggerFactory.getLogger(ChangeLogRunner::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            logger.info("STARTING : Spring boot application starting")
            SpringApplicationBuilder(ChangeLogRunner::class.java)
                    .properties(
                            "spring.config.name:application,core"
                    )
                    .bannerMode(Banner.Mode.OFF)
                    .build()
                    .run(*args)
            logger.info("STOPPED  : Spring boot application stopped")
        }
    }

    override fun run(args: ApplicationArguments?) {
        logger.info("STARTING : Spring boot application starting")
    }

    @Bean
    open fun liquibase(): SpringLiquibase? {
        val liquibase = SpringLiquibase()
        liquibase.changeLog = args?.getOptionValues("changeLog")?.first() ?: "classpath:init-changeLog.xml"
        liquibase.dataSource = if (args?.getOptionValues("dataSource")?.first() == "replica")
            dataSourceFactory.create(properties.replicaDataSource)
        else
            dataSourceFactory.create(properties.primaryDataSource)

        return liquibase
    }
}




