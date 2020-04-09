package com.github.pcfleischer.eventbroker.configuration

import com.github.pcfleischer.eventbroker.core.CoreProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties
@EnableConfigurationProperties
class ApiProperties : CoreProperties()