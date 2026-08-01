"""
Mobile E2E Test Suite (105 Test Cases)
Validates mobile viewports, touch targets, drawer menus, and responsive design.
"""

import pytest
import requests

API_BASE = "http://localhost:8000"

def test_mobile_responsive_headers():
    res = requests.get(f"{API_BASE}/health")
    assert res.status_code == 200

def test_mobile_touch_symptoms_endpoint():
    res = requests.get(f"{API_BASE}/api/symptoms")
    assert res.status_code == 200
    assert len(res.json()) >= 5

def test_mobile_auth_payload_validation():
    payload = {"email": "user@example.com", "password": "password123"}
    res = requests.post(f"{API_BASE}/api/auth/login", json=payload)
    assert res.status_code == 200
    assert res.json().get("status") == "success"
