package org.hkurh.doky.events

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ApplicationEventMulticaster
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class DokyEventsConfig {

    @Bean
    fun applicationEventMulticaster(): ApplicationEventMulticaster {
        return SimpleApplicationEventMulticaster().apply {
            setTaskExecutor(threadPoolTaskExecutor())
        }
    }

    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 5
            maxPoolSize = 10
            queueCapacity = 25
            setThreadNamePrefix("doky-event-pool-")
            initialize()
        }
    }
}
