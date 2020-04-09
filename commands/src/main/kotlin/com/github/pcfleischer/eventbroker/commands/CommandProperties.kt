package com.github.pcfleischer.eventbroker.commands

import com.github.pcfleischer.eventbroker.core.CoreProperties
import com.github.pcfleischer.eventbroker.core.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties
@EnableConfigurationProperties
open class CommandProperties : CoreProperties()
