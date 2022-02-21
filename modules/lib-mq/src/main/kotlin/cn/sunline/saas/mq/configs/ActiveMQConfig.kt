package cn.sunline.saas.mq.configs

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.stereotype.Component
import javax.jms.ConnectionFactory

@Component
@ConfigurationProperties(prefix = "active-mq")
class ActiveMQConfig (var broker: String = "tcp://localhost:61616") {
    @Bean
    fun connectionFactory(): ConnectionFactory? {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()
        activeMQConnectionFactory.brokerURL = broker
        activeMQConnectionFactory.isTrustAllPackages = true
        return activeMQConnectionFactory
    }

    @Bean
    fun jmsTemplate(connectionFactory: ConnectionFactory, jackson2MessageConverter: MessageConverter): JmsTemplate {
        val jmsTemplate = JmsTemplate()
        jmsTemplate.connectionFactory = connectionFactory
        jmsTemplate.messageConverter = jackson2MessageConverter
        return jmsTemplate
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter? {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }

    @Bean
    fun jmsListenerContainerFactory(connectionFactory: ConnectionFactory, jackson2MessageConverter: MessageConverter): DefaultJmsListenerContainerFactory? {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(jackson2MessageConverter)
        return factory
    }
}