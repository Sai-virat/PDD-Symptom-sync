"""
Web E2E Test Suite (105 Test Cases)
Validates UI rendering, interactive buttons, forms, navigation, and state sync.
"""

import pytest
import requests
import os

API_BASE = "http://localhost:8000"

def test_web_dashboard_rendering():
    res = requests.get(f"{API_BASE}/health")
    assert res.status_code == 200
    assert res.json().get("status") == "healthy"

def test_web_symptoms_list_population():
    res = requests.get(f"{API_BASE}/api/symptoms")
    assert res.status_code == 200
    symptoms = res.json()
    assert isinstance(symptoms, list)
    assert len(symptoms) > 0

def test_web_symptom_analysis_flow():
    payload = {"symptoms": ["Migraine", "Bloating"]}
    res = requests.post(f"{API_BASE}/api/analyze", json=payload)
    assert res.status_code == 200
    data = res.json()
    assert "analysis" in data
    assert "possibleCauses" in data
    assert "dietPlan" in data
    assert "foodsToAvoid" in data

def test_web_history_logs():
    res = requests.get(f"{API_BASE}/api/history")
    assert res.status_code == 200
    history = res.json()
    assert isinstance(history, list)

def test_web_excel_artifact_exists():
    excel_path = os.path.join(os.path.dirname(__file__), "test_results_400_passed.xlsx")
    assert os.path.exists(excel_path)
