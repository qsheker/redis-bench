package org.qsheker.benchmarkredis.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qsheker.benchmarkredis.domain.CacheMode;
import org.qsheker.benchmarkredis.domain.db.Product;
import org.qsheker.benchmarkredis.domain.service.ProductService;
import org.qsheker.benchmarkredis.domain.service.impl.DbProductService;
import org.qsheker.benchmarkredis.domain.service.impl.ManualCachingProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final DbProductService productService;
    private final ManualCachingProductService manualCachingProductService;

    @PostMapping
    public ResponseEntity<Product> create(
            @RequestBody ProductCreateRequest request,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE")CacheMode cacheMode) {
        log.info("Creating product with cacheMode={}", cacheMode);

        ProductService service = modeResolver(cacheMode);

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product saved = service.create(product);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(
            @PathVariable Long id,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE")CacheMode cacheMode) {
        log.info("Received get request for id={}, cacheMode={}",id, cacheMode);

        ProductService service = modeResolver(cacheMode);

        Product product = service.getById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE")CacheMode cacheMode) {
        log.info("Received update request for id={}, cacheMode={}", id, cacheMode);

        ProductService service = modeResolver(cacheMode);

        Product product = Product.builder()
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product updated = service.update(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE")CacheMode cacheMode) {
        log.info("Received delete request for id={}, cacheMode={}",id, cacheMode);
        ProductService service = modeResolver(cacheMode);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProductService modeResolver(CacheMode cacheMode) {
        return switch (cacheMode){
            case NONE_CACHE -> productService;
            case MANUAL -> manualCachingProductService;
        };
    }
}
