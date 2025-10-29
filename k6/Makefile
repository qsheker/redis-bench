# Makefile for running k6 load tests

BASE_URL ?= http://localhost:8081
CACHE_MODE ?= MANUAL

.PHONY: help
help:
	@echo "Commands to run k6 tests:"
	@echo "  make post      - Test creating products"
	@echo "  make get       - Test fetching a product by ID"
	@echo "  make put       - Test updating a product"
	@echo "  make delete    - Test deleting a product"
	@echo "  make all       - Run all tests"
	@echo ""
	@echo "Settings:"
	@echo "  BASE_URL       - Server URL (default: http://localhost:8080)"
	@echo "  CACHE_MODE     - Caching mode (default: MANUAL)"
	@echo ""
	@echo "Examples:"
	@echo "  make get CACHE_MODE=NONE_CACHE"
	@echo "  make post BASE_URL=http://prod-server:8080"

# Test creating products
.PHONY: post
post:
	@echo "Running product creation test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-post.js

# Test fetching a product by ID
.PHONY: get
get:
	@echo "Running product fetch test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-get.js

.PHONY: get-none-cache
get-none-cache:
	@echo "Running product fetch test (mode=NONE_CACHE)..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=NONE_CACHE k6 run test-get.js

.PHONY: get-manual-cache
get-manual-cache:
	@echo "Running product fetch test (mode=MANUAL)..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=MANUAL k6 run test-get.js

.PHONY: get-spring-cache
get-spring-cache:
	@echo "Running product fetch test (mode=SPRING)..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=SPRING k6 run test-get.js

# Test updating a product
.PHONY: put
put:
	@echo "Running product update test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-put.js

# Test deleting a product
.PHONY: delete
delete:
	@echo "Running product deletion test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-delete.js

# Run all tests sequentially
.PHONY: all
all: post get put delete
	@echo "All tests completed!"
