from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr
from typing import List, Dict, Optional
import uvicorn
from firebase_setup import db

app = FastAPI(title="SymptomSync API")

# Enable CORS for Next.js frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify the actual domain
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# --- Models ---

class UserLogin(BaseModel):
    email: str
    password: str

class PossibleCause(BaseModel):
    title: str
    description: str

class MealDetailSpec(BaseModel):
    name: str
    description: str
    calories: str
    protein: str
    fiber: str
    time: str

class SymptomInfo(BaseModel):
    name: str
    possibleCauses: List[PossibleCause]
    dietPlan: Dict[str, MealDetailSpec]
    foodsToAvoid: List[str]

# --- Endpoints ---

@app.post("/auth/login")
async def login(user: UserLogin):
    # Simple validation for demonstration
    if "@" not in user.email:
        raise HTTPException(status_code=400, detail="Invalid email format")

    # In a real app, verify against database
    if user.email == "user@example.com" and user.password == "password123":
        return {"status": "success", "user": {"name": "John Doe", "email": user.email}}

    raise HTTPException(status_code=401, detail="Invalid credentials")

@app.get("/symptoms")
async def get_symptoms():
    try:
        docs = db.collection("symptoms").stream()
        return [doc.to_dict().get("name") for doc in docs]
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")

@app.post("/analyze")
async def analyze_symptoms(selected: List[str]):
    results = []
    detected_infos = []
    possible_causes = []
    foods_to_avoid = set()

    try:
        for name in selected:
            # Query Firestore for the symptom
            doc_id = name.lower().replace(" ", "_")
            doc = db.collection("symptoms").document(doc_id).get()

            if doc.exists:
                info = doc.to_dict()
                results.append({"name": name, "severity": "Medium"})
                detected_infos.append(info)
                for cause in info.get("possibleCauses", []):
                    if cause not in possible_causes:
                        possible_causes.append(cause)
                for food in info.get("foodsToAvoid", []):
                    foods_to_avoid.add(food)

        # Generate Diet Plan based on primary symptom
        diet_plan = []
        if detected_infos:
            primary = detected_infos[0]
            diet_data = primary.get("dietPlan", {})
            for meal_type, spec in diet_data.items():
                diet_plan.append({"title": meal_type, "time": spec.get("time", "Scheduled"), "details": spec})

        return {
            "analysis": results,
            "possibleCauses": possible_causes,
            "dietPlan": diet_plan,
            "foodsToAvoid": list(foods_to_avoid)
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Analysis error: {str(e)}")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
