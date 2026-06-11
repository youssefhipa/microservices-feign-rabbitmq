package com.scalable.enrollment.messaging;

import com.scalable.enrollment.config.RabbitTopologyConfig;
import com.scalable.enrollment.domain.Enrollment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EnrollmentPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishConfirmed(Enrollment enrollment) {
        EnrollmentConfirmedEvent event = new EnrollmentConfirmedEvent(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getSectionId(),
                enrollment.getAmount()
        );
        rabbitTemplate.convertAndSend(
                RabbitTopologyConfig.ENROLLMENTS_EXCHANGE,
                RabbitTopologyConfig.ENROLLMENT_CONFIRMED_ROUTING_KEY,
                event
        );
    }
}
