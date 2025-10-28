package org.qsheker.benchmarkredis.domain.service;

import org.qsheker.benchmarkredis.domain.db.Product;

public interface ProductService {
    Product create(Product createRequest);
    Product update(Long id, Product updateRequest);
    Product getById(Long id);
    void delete(Long id);
}
