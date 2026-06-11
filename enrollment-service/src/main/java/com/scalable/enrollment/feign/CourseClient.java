package com.scalable.enrollment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "course-service",
        url = "${course.service.url:http://course-service:8081}"
)
public interface CourseClient {

    @PostMapping("/api/courses/sections/{sectionId}/reserve")
    SectionDto reserve(@PathVariable("sectionId") Long sectionId);
}
