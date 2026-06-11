package com.scalable.enrollment.feign;

import java.math.BigDecimal;

public record SectionDto(Long id, String title, int availableSeats, BigDecimal tuition) {
}
