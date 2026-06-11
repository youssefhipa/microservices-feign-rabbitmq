package com.scalable.notifications.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    public static final String ENROLLMENTS_EXCHANGE = "enrollments.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String ENROLLMENT_CONFIRMED_ROUTING_KEY = "enrollment.confirmed";

    @Bean
    TopicExchange enrollmentsExchange() {
        return new TopicExchange(ENROLLMENTS_EXCHANGE, true, false);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true, false, false);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, TopicExchange enrollmentsExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(enrollmentsExchange)
                .with(ENROLLMENT_CONFIRMED_ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
