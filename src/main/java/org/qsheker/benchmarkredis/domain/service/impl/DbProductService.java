package org.qsheker.benchmarkredis.domain.service.impl;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qsheker.benchmarkredis.domain.db.Product;
import org.qsheker.benchmarkredis.domain.db.ProductRepository;
import org.qsheker.benchmarkredis.domain.service.ProductService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product create(Product createRequest) {
        log.info("Creating product in DB: {}", createRequest.getName());
        Product product = Product.builder()
                .name(createRequest.getName())
                .price(createRequest.getPrice())
                .description(createRequest.getDescription())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product updateRequest) {
        log.info("Updating product in DB: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        if (updateRequest.getPrice() != null) {
            product.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getDescription() != null) {
            product.setDescription(updateRequest.getDescription());
        }

        return productRepository.save(product);
    }

    @Override
    public Product getById(Long id) {
        log.info("Getting product from DB: id={}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }


    @Override
    public void delete(Long id) {
        log.info("Deleting product from DB: {}", id);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
