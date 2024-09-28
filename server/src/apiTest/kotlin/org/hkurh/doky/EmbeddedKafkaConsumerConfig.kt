package org.hkurh.doky

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class EmbeddedKafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private var bootstrapServer: String = ""

    @Value("\${spring.kafka.properties.security.protocol}")
    private var securityProtocol: String = ""

    @Value("\${spring.kafka.properties.sasl.mechanism}")
    private var saslMechanism: String = ""

    @Value("\${spring.kafka.properties.sasl.jaas.config}")
    private var saslConfig: String = ""

    @Bean
    fun kafkaConsumer(): Consumer<String, Any> {
        val configProps = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to securityProtocol,
            SaslConfigs.SASL_MECHANISM to saslMechanism,
            SaslConfigs.SASL_JAAS_CONFIG to saslConfig,
            ConsumerConfig.GROUP_ID_CONFIG to "test-group",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            JsonDeserializer.TRUSTED_PACKAGES to "org.hkurh.doky.kafka",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java
        )

        return KafkaConsumer(configProps)
    }
}
