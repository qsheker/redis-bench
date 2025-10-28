package org.qsheker.benchmarkredis.api;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ProductDto {
    Long id;
    String name;
    BigDecimal price;
    String description;
    Instant createdAt;
    Instant updatedAt;
}
