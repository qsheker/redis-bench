package org.qsheker.benchmarkredis.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qsheker.benchmarkredis.domain.db.Product;
import org.qsheker.benchmarkredis.domain.db.ProductRepository;
import org.qsheker.benchmarkredis.domain.service.ProductService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManualCachingProductService implements ProductService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Product> redisTemplate;

    private static final String CACHE_KEY_PREFIX = "product:";
    private static final long CACHE_TTL_MINUTES = 1;

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

        var savedProduct = productRepository.save(product);

        String cacheKey = CACHE_KEY_PREFIX+id;
        redisTemplate.delete(cacheKey);
        log.info("Product deleted from cache: id={}", id);

        return savedProduct;
    }

    @Override
    public Product getById(Long id) {
        log.info("Getting product: id={}", id);
        var cacheKey = CACHE_KEY_PREFIX+id;

        Product objectFromCache = redisTemplate.opsForValue()
                .get(cacheKey);

        if(objectFromCache!=null){
            log.info("Product found in cache: id={}", id);
            return objectFromCache;
        }
        log.info("Product not found in cache: id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        redisTemplate.opsForValue()
                .set(cacheKey, product,CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        log.info("Product cached: id={}", id);
        return product;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting product from DB: {}", id);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);

        String cacheKey = CACHE_KEY_PREFIX+id;
        redisTemplate.delete(cacheKey);
        log.info("Product deleted from cache: id={}", id);
    }

}
