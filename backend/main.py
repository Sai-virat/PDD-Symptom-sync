import os
from fastapi import FastAPI, HTTPException, Depends, Body, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from fastapi.responses import FileResponse, JSONResponse
from pydantic import BaseModel, EmailStr
from typing import List, Dict, Optional, Union
import uvicorn

from firebase_setup import db
from symptoms_data import SYMPTOMS_DATA

app = FastAPI(title="SymptomSync Unified Server", version="1.0.0")

# Enable CORS for cross-origin requests
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# --- Request / Response Models ---

class UserLogin(BaseModel):
    email: str
    password: str

class UserRegister(BaseModel):
    name: str
    email: str
    password: str

class SymptomAnalysisRequest(BaseModel):
    symptoms: List[str]

# --- API Endpoints (Prefix: /api/ and fallback /) ---

@app.get("/api/health")
@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "firebase_connected": db is not None,
        "total_symptoms": len(SYMPTOMS_DATA)
    }

@app.post("/api/auth/login")
@app.post("/auth/login")
async def login(user: UserLogin):
    if not user.email or "@" not in user.email:
        raise HTTPException(status_code=400, detail="Invalid email format")

    if user.email == "user@example.com" and user.password == "password123":
        return {
            "status": "success",
            "token": "demo-jwt-token-symptomsync",
            "user": {
                "name": "John Doe",
                "email": user.email,
                "preferences": ["Low Histamine", "Gluten-Free"]
            }
        }
    
    if len(user.password) >= 6:
        return {
            "status": "success",
            "token": "demo-jwt-token-symptomsync",
            "user": {
                "name": user.email.split("@")[0].capitalize(),
                "email": user.email,
                "preferences": []
            }
        }

    raise HTTPException(status_code=401, detail="Invalid email or password")

@app.post("/api/auth/register")
@app.post("/auth/register")
async def register(user: UserRegister):
    if not user.email or "@" not in user.email:
        raise HTTPException(status_code=400, detail="Invalid email address format")

    if not user.name or len(user.name.strip()) < 2:
        raise HTTPException(status_code=400, detail="Full name must be at least 2 characters long")

    if not user.password or len(user.password) < 6:
        raise HTTPException(status_code=400, detail="Password must be at least 6 characters long")

    if db is not None:
        try:
            doc_id = user.email.lower().strip()
            user_ref = db.collection("users").document(doc_id)
            user_ref.set({
                "name": user.name.strip(),
                "email": user.email.lower().strip(),
                "created_at": "2026-08-01"
            })
        except Exception as e:
            print(f"[Notice] Firebase registration sync notice: {e}")

    return {
        "status": "success",
        "message": "Account created successfully",
        "token": "demo-jwt-token-symptomsync",
        "user": {
            "name": user.name.strip(),
            "email": user.email.lower().strip(),
            "preferences": []
        }
    }

@app.get("/api/symptoms")
@app.get("/symptoms")
async def get_symptoms():
    if db is not None:
        try:
            docs = db.collection("symptoms").stream()
            names = [doc.to_dict().get("name") for doc in docs if doc.to_dict().get("name")]
            if names:
                return names
        except Exception as e:
            print(f"[Notice] Firestore fetch failed: {e}. Falling back to in-memory dataset.")

    return [item["name"] for item in SYMPTOMS_DATA]

@app.post("/api/analyze")
@app.post("/analyze")
async def analyze_symptoms(payload: Union[SymptomAnalysisRequest, List[str]] = Body(...)):
    if isinstance(payload, SymptomAnalysisRequest):
        selected = payload.symptoms
    elif isinstance(payload, list):
        selected = payload
    else:
        selected = getattr(payload, "symptoms", [])

    results = []
    detected_infos = []
    possible_causes = []
    foods_to_avoid = set()

    fallback_map = {item["name"].lower(): item for item in SYMPTOMS_DATA}

    try:
        for name in selected:
            info = None
            if db is not None:
                try:
                    doc_id = name.lower().replace(" ", "_")
                    doc = db.collection("symptoms").document(doc_id).get()
                    if doc.exists:
                        info = doc.to_dict()
                except Exception:
                    pass

            if not info:
                info = fallback_map.get(name.lower())

            if info:
                results.append({"name": name, "severity": "Medium"})
                detected_infos.append(info)
                for cause in info.get("possibleCauses", []):
                    if cause not in possible_causes:
                        possible_causes.append(cause)
                for food in info.get("foodsToAvoid", []):
                    foods_to_avoid.add(food)
            else:
                results.append({"name": name, "severity": "Medium"})
                possible_causes.append({
                    "title": f"General {name} Triggers",
                    "description": f"Common metabolic or lifestyle factors associated with {name}."
                })

        diet_plan = []
        if detected_infos:
            primary = detected_infos[0]
            diet_data = primary.get("dietPlan", {})
            for meal_type, spec in diet_data.items():
                if isinstance(spec, dict):
                    diet_plan.append({
                        "title": meal_type,
                        "time": spec.get("time", "Scheduled"),
                        "details": spec
                    })

        return {
            "analysis": results,
            "possibleCauses": possible_causes,
            "dietPlan": diet_plan,
            "foodsToAvoid": list(foods_to_avoid)
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Analysis error: {str(e)}")

@app.get("/api/history")
@app.get("/history")
async def get_history():
    return [
        {"id": 1, "date": "Today, 9:30 AM", "symptom": "Migraine", "severity": "High", "cause": "Tyramine Foods"},
        {"id": 2, "date": "Yesterday, 2:15 PM", "symptom": "Bloating", "severity": "Medium", "cause": "Digestive Sensitivity"},
        {"id": 3, "date": "Jul 22, 2026", "symptom": "Acidity", "severity": "Low", "cause": "Late Dinner"}
    ]

# --- Static Frontend Page Serving ---
out_dir = os.path.join(os.path.dirname(__file__), "..", "frontend", "out")

if os.path.exists(out_dir):
    _next_dir = os.path.join(out_dir, "_next")
    if os.path.exists(_next_dir):
        app.mount("/_next", StaticFiles(directory=_next_dir), name="next_static")

@app.get("/{full_path:path}")
async def serve_frontend(full_path: str):
    if full_path.startswith("api/"):
        return JSONResponse({"detail": "API endpoint not found"}, status_code=404)

    clean_path = full_path.strip("/")

    # Check direct file match
    file_path = os.path.join(out_dir, clean_path)
    if clean_path and os.path.exists(file_path) and os.path.isfile(file_path):
        return FileResponse(file_path)

    # Handle Next.js RSC .txt prefetch requests
    base_clean = clean_path[:-4] if clean_path.endswith(".txt") else clean_path

    # Check html file match (e.g. analyze -> analyze.html)
    html_file = os.path.join(out_dir, f"{base_clean}.html")
    if base_clean and os.path.exists(html_file):
        return FileResponse(html_file)

    # Fallback to index.html for SPA routes
    index_file = os.path.join(out_dir, "index.html")
    if os.path.exists(index_file):
        return FileResponse(index_file)

    return JSONResponse({"message": "SymptomSync Server Running"}, status_code=200)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
