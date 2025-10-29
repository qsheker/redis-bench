Redis Benchmarking with k6

This project provides a set of load testing scripts for benchmarking your Redis-based caching in a Spring Boot application. It tests different caching modes (NONE_CACHE and MANUAL) by performing CRUD operations on Product entities and comparing performance.

---

Table of Contents

* Requirements
* Setup
* Makefile Commands
* k6 Test Scripts
* Configuration
* Benchmark Results
* Visualizing Results
* Notes

---

Requirements

* Node.js (for running k6 scripts locally)
* k6 (load testing tool)
* Java Spring Boot application with Redis caching
* Make (optional, for Makefile commands)

---

Setup

1. Clone this repository:

git clone <repository-url>
cd <repository-folder>

2. Install k6 (if not already installed):

brew install k6      # macOS
choco install k6     # Windows

3. Make sure your Spring Boot server is running and accessible via BASE_URL.

---

Makefile Commands

Run tests using the Makefile:

make post          # Test creating products
make get           # Test fetching products by ID
make put           # Test updating products
make delete        # Test deleting products
make all           # Run all tests sequentially

Specify caching mode:

make get CACHE_MODE=NONE_CACHE
make get CACHE_MODE=MANUAL

Or specify a different server URL:

make post BASE_URL=[http://localhost:8081](http://localhost:8081)

---

k6 Test Scripts

1. POST (test-post.js) – Creates products with random data.
2. GET (test-get.js) – Reads products using popular random IDs to simulate real load.
3. PUT (test-put.js) – Updates product price and description with random values.
4. DELETE (test-delete.js) – Creates and immediately deletes products to test delete operations.
5. Utils (k6_utils.js) – Common functions for generating random IDs, strings, floats, and choices.

---

Configuration

All k6 scripts use config.js for global configuration:

export const BASE_URL = '[http://localhost:8081](http://localhost:8081)';

export const CACHE_MODE = 'MANUAL'; // options: NONE_CACHE, MANUAL

---

Benchmark Results

Tests were performed for both caching modes (NONE_CACHE and MANUAL) with the following observations:

* NONE_CACHE:

  * Higher average response times
  * More frequent database hits
  * Baseline performance without caching

* MANUAL:

  * Lower average response times
  * Fewer database accesses due to Redis caching
  * Demonstrates effectiveness of manual caching

Results can be exported from k6 in JSON or CSV format for further analysis.

---

Visualizing Results

### Generate k6 HTML Report

```bash
k6 run test-get.js --out json=results_none_cache.json
k6 run test-get.js --out json=results_manual.json
k6 html results_none_cache.json -o results_none_cache.html
k6 html results_manual.json -o results_manual.html
```

Open the HTML files in a browser to see interactive graphs of:

* Response times
* Requests per second
* HTTP status codes
* Failures

### Compare Modes Side-by-Side

1. Convert JSON results to CSV:

```bash
k6 convert-to-csv results_none_cache.json results_none_cache.csv
k6 convert-to-csv results_manual.json results_manual.csv
```

2. Load CSV into a spreadsheet or Python and create charts for:

* Response time comparison
* Throughput (requests/sec)
* Successful vs failed requests

### Example Python Visualization

```python
import pandas as pd
import matplotlib.pyplot as plt

none_cache = pd.read_csv("results_none_cache.csv")
manual_cache = pd.read_csv("results_manual.csv")

plt.plot(none_cache['time'], none_cache['http_req_duration'], label='NONE_CACHE')
plt.plot(manual_cache['time'], manual_cache['http_req_duration'], label='MANUAL')
plt.xlabel('Time (s)')
plt.ylabel('Response Time (ms)')
plt.title('Response Time Comparison')
plt.legend()
plt.show()
```

This gives a clear visualization of caching impact on response times over the test duration.

---

## Benchmark Results Screenshots

**NONE_CACHE Mode**
<img width="955" height="314" alt="image" src="https://github.com/user-attachments/assets/c9d2cdfa-0a9f-4a3b-a6b1-83b2bf467625" />

**MANUAL Mode**
<img width="1006" height="327" alt="image" src="https://github.com/user-attachments/assets/5616b3db-6d12-4678-acc6-6873d09ab1ff" />


Notes

* Each virtual user generates its own set of popular IDs to simulate realistic access patterns.
* Use stages in k6 scripts to simulate ramp-up, sustain, and ramp-down for realistic load testing.
* Adjust target in stages to control the number of virtual users.
* MANUAL caching consistently reduces response times and database load compared to NONE_CACHE.

---

License

This project is licensed under the MIT License.
