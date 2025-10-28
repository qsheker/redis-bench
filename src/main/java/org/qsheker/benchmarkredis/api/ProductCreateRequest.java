package org.qsheker.benchmarkredis.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateRequest {
    String name;
    BigDecimal price;
    String description;
}
