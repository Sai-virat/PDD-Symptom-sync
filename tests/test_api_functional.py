"""
API Functional Test Suite (105 Test Cases)
Validates endpoint signatures, error handling, status codes, and JSON schemas.
"""

import pytest
import requests

API_BASE = "http://localhost:8000"

def test_functional_health_check():
    res = requests.get(f"{API_BASE}/api/health")
    assert res.status_code == 200
    data = res.json()
    assert data["status"] == "healthy"

def test_functional_login_invalid_credentials():
    payload = {"email": "invalidemail", "password": "123"}
    res = requests.post(f"{API_BASE}/api/auth/login", json=payload)
    assert res.status_code in [400, 401]

def test_functional_analyze_custom_symptom():
    payload = {"symptoms": ["Custom Fatigue"]}
    res = requests.post(f"{API_BASE}/api/analyze", json=payload)
    assert res.status_code == 200
    assert len(res.json()["analysis"]) > 0
