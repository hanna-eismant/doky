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

    @Bean
    fun consumerFactory(): DefaultKafkaConsumerFactory<String, SendEmailMessage> {
        val configProps = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to securityProtocol,
            SaslConfigs.SASL_MECHANISM to saslMechanism,
            SaslConfigs.SASL_JAAS_CONFIG to saslConfig,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to pullInterval,
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
        return factory
    }
}
