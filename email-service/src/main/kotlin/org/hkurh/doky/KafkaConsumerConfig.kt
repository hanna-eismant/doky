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
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.hkurh.doky.kafka.SendEmailMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.JsonDeserializer


@Configuration
@EnableKafka
class KafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private var bootstrapServer: String = ""

    @Value("\${spring.kafka.properties.security.protocol}")
    private var securityProtocol: String = ""

    @Value("\${spring.kafka.properties.sasl.mechanism}")
    private var saslMechanism: String = ""

    @Value("\${spring.kafka.properties.sasl.jaas.config}")
    private var saslConfig: String = ""

    @Value("\${doky.kafka.emails.group.id}")
    private var groupId: String = ""

    @Value("\${spring.kafka.properties.max.poll.interval.ms}")
    private var pullInterval: Int = 300000

    @Value("\${spring.kafka.properties.auto.offset.reset}")
    private var autoOffsetReset: String = "earliest"

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
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "org.hkurh.doky.kafka",
            JsonDeserializer.VALUE_DEFAULT_TYPE to SendEmailMessage::class.java
        )
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, SendEmailMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, SendEmailMessage>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
        return factory
    }
}
