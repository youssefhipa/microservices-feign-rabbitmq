package com.scalable.enrollment.service;

import com.scalable.enrollment.domain.Enrollment;
import com.scalable.enrollment.domain.EnrollmentRepository;
import com.scalable.enrollment.feign.CourseClient;
import com.scalable.enrollment.feign.SectionDto;
import com.scalable.enrollment.messaging.EnrollmentPublisher;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollments;
    private final CourseClient courseClient;
    private final EnrollmentPublisher enrollmentPublisher;

    public EnrollmentService(
            EnrollmentRepository enrollments,
            CourseClient courseClient,
            EnrollmentPublisher enrollmentPublisher
    ) {
        this.enrollments = enrollments;
        this.courseClient = courseClient;
        this.enrollmentPublisher = enrollmentPublisher;
    }

    public Enrollment create(Long studentId, Long sectionId) {
        Enrollment enrollment = enrollments.save(new Enrollment(studentId, sectionId));
        SectionDto section = courseClient.reserve(sectionId);
        enrollment.confirm(section.tuition());
        Enrollment confirmedEnrollment = enrollments.save(enrollment);
        enrollmentPublisher.publishConfirmed(confirmedEnrollment);
        return confirmedEnrollment;
    }
}
