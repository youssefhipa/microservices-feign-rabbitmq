package com.scalable.enrollment.messaging;

import java.math.BigDecimal;

public record EnrollmentConfirmedEvent(
        Long enrollmentId,
        Long studentId,
        Long sectionId,
        BigDecimal amount
) {
}
