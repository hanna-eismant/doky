/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.hkurh.doky.kafka.SendEmailMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.CommonErrorHandler
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.util.backoff.FixedBackOff

/**
 * Configuration class for setting up Kafka consumer properties and listeners.
 * This class is responsible for configuring the Kafka consumer factory and listener container factory
 * to enable message consumption from Kafka topics.
 *
 * Annotations Used:
 * - `@Configuration`: Indicates that this class declares one or more `@Bean` methods and is processed by Spring.
 * - `@EnableKafka`: Enables Kafka-related features in the Spring context.
 *
 * Features:
 * - Configures various Kafka consumer properties such as bootstrap servers, security protocol,
 *   SASL mechanism, group id, poll interval, and offset reset strategy.
 * - Defines a consumer factory bean for creating Kafka consumers with specified configurations.
 * - Configures the `ConcurrentKafkaListenerContainerFactory` to manage Kafka message listeners.
 * - Enables RECORD acknowledgment mode for message consumption.
 */
@Configuration
@EnableKafka
class KafkaConsumerConfig {

    private val log = KotlinLogging.logger {}

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServer: String

    @Value("\${spring.kafka.properties.security.protocol}")
    private lateinit var securityProtocol: String

    @Value("\${spring.kafka.properties.sasl.mechanism}")
    private lateinit var saslMechanism: String

    @Value("\${spring.kafka.properties.sasl.jaas.config}")
    private lateinit var saslConfig: String

    @Value("\${doky.kafka.emails.group.id}")
    private lateinit var groupId: String

    @Value("\${spring.kafka.properties.max.poll.interval.ms}")
    private var pullInterval: Int = 300000

    @Value("\${spring.kafka.properties.auto.offset.reset}")
    private lateinit var autoOffsetReset: String

    @Bean
    fun consumerFactory(): DefaultKafkaConsumerFactory<String, SendEmailMessage> {
        val configProps = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to securityProtocol,
            SaslConfigs.SASL_MECHANISM to saslMechanism,
            SaslConfigs.SASL_JAAS_CONFIG to saslConfig,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to pullInterval,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to autoOffsetReset,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java.name,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java.name,
            ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS to StringDeserializer::class.java,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "org.hkurh.doky.kafka",
            JsonDeserializer.VALUE_DEFAULT_TYPE to SendEmailMessage::class.java
        )
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(kafkaTemplate: KafkaTemplate<String, Any>): ConcurrentKafkaListenerContainerFactory<String, SendEmailMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, SendEmailMessage>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
        factory.setCommonErrorHandler(kafkaErrorHandler(kafkaTemplate))
        return factory
    }

    @Bean
    fun kafkaErrorHandler(kafkaTemplate: KafkaTemplate<String, Any>): CommonErrorHandler {
        val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { consumerRecord, exception ->
            log.error { "Sending message [${consumerRecord.key()}] to DLT [${consumerRecord.topic()}.DLT] due to exception: [${exception.message}]" }
            TopicPartition("${consumerRecord.topic()}.DLT", consumerRecord.partition())
        }

        return DefaultErrorHandler(recoverer, FixedBackOff(1000L, 3))
    }
}
