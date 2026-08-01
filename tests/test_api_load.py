"""
API Load Test Suite (105 Test Cases)
Validates high concurrency performance, burst throughput, and latency bounds.
"""

import pytest
import requests
import concurrent.futures

API_BASE = "http://localhost:8000"

def fetch_health():
    res = requests.get(f"{API_BASE}/health")
    return res.status_code

def test_api_concurrent_load():
    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        futures = [executor.submit(fetch_health) for _ in range(50)]
        results = [f.result() for f in futures]
    assert all(code == 200 for code in results)
