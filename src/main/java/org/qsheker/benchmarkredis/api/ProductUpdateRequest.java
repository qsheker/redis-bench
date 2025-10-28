package org.qsheker.benchmarkredis.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    BigDecimal price;
    String description;
}
