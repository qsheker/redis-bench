package org.qsheker.benchmarkredis.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qsheker.benchmarkredis.domain.db.Product;
import org.qsheker.benchmarkredis.domain.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductCreateRequest request) {
        log.info("Received create request: {}", request.getName());

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product saved = productService.create(product);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        log.info("Received get request for id={}", id);
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        log.info("Received update request for id={}", id);

        Product product = Product.builder()
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product updated = productService.update(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received delete request for id={}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
