package ro.acs.ssa.cc.mailing.mailcreatorservice.config;

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
    @Value("${ro.acs.ssa.cc.mailing.send-messages-queue.queueName}")
    private String sendMessagesQueueName;
    @Value("${ro.acs.ssa.cc.mailing.send-messages-queue.exchange}")
    private String sendMessagesQueueExchange;
    @Value("${ro.acs.ssa.cc.mailing.send-messages-queue.routingKey}")
    private String sendMessagesQueueRoutingKey;

    @Bean
    public Queue queue() {
        return new Queue(sendMessagesQueueName);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(sendMessagesQueueExchange);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(sendMessagesQueueRoutingKey);
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
