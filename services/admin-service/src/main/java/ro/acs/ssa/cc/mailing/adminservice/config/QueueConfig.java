package ro.acs.ssa.cc.mailing.adminservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    @Value("${ro.acs.ssa.cc.mailing.build-messages-queue.queueName}")
    private String buildMessagesQueueName;
    @Value("${ro.acs.ssa.cc.mailing.build-messages-queue.exchange}")
    private String buildMessagesQueueExchange;
    @Value("${ro.acs.ssa.cc.mailing.build-messages-queue.routingKey}")
    private String buildMessagesQueueRoutingKey;

    @Bean
    public Queue queue() {
        return new Queue(buildMessagesQueueName);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(buildMessagesQueueExchange);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(buildMessagesQueueRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
