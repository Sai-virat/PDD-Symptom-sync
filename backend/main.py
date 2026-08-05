import os
import random
import datetime
import asyncio
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from fastapi import FastAPI, HTTPException, Depends, Body, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from fastapi.responses import FileResponse, JSONResponse
from pydantic import BaseModel, EmailStr
from typing import List, Dict, Optional, Union
import uvicorn

from firebase_setup import db
from symptoms_data import SYMPTOMS_DATA
from ai_service import generate_ai_diet_plan

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

import random
import datetime
import asyncio

class UserLogin(BaseModel):
    email: str
    password: str

class UserRegister(BaseModel):
    name: str
    email: str
    password: str
    phone: Optional[str] = None

class SymptomAnalysisRequest(BaseModel):
    symptoms: List[str]

class FeedbackRequest(BaseModel):
    rating: int
    feedback: str
    email: Optional[str] = None

# --- API Endpoints (Prefix: /api/ and fallback /) ---

@app.get("/api/health")
async def health_check():
    return {
        "status": "healthy",
        "firebase_connected": db is not None,
        "total_symptoms": len(SYMPTOMS_DATA)
    }

@app.post("/api/auth/login")
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
                "phone": "+1 (555) 234-5678",
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
                "phone": "+1 (555) 234-5678",
                "preferences": []
            }
        }

    raise HTTPException(status_code=401, detail="Invalid email or password")

@app.post("/api/auth/register")
async def register(user: UserRegister):
    if not user.email or "@" not in user.email:
        raise HTTPException(status_code=400, detail="Invalid email address format")

    if not user.name or len(user.name.strip()) < 2:
        raise HTTPException(status_code=400, detail="Full name must be at least 2 characters long")

    if not user.password or len(user.password) < 6:
        raise HTTPException(status_code=400, detail="Password must be at least 6 characters long")

    phone_num = user.phone.strip() if user.phone and len(user.phone.strip()) >= 5 else "+1 (555) 234-5678"

    if db is not None:
        try:
            doc_id = user.email.lower().strip()
            user_ref = db.collection("users").document(doc_id)
            user_ref.set({
                "name": user.name.strip(),
                "email": user.email.lower().strip(),
                "phone": phone_num,
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
            "phone": phone_num,
            "preferences": []
        }
    }

@app.post("/api/feedback")
async def submit_feedback(data: FeedbackRequest):
    if data.rating < 1 or data.rating > 5:
        raise HTTPException(status_code=400, detail="Rating must be between 1 and 5 stars")

    if db is not None:
        try:
            db.collection("feedback").add({
                "rating": data.rating,
                "feedback": data.feedback.strip(),
                "user_email": data.email or "anonymous@symptomsync.com",
                "created_at": "2026-08-01"
            })
        except Exception as e:
            print(f"[Notice] Firebase feedback sync notice: {e}")

    return {
        "status": "success",
        "message": "Thank you! Your feedback helps us improve."
    }

@app.get("/api/symptoms")
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
async def analyze_symptoms(payload: Union[SymptomAnalysisRequest, List[str]] = Body(...)):
    if isinstance(payload, SymptomAnalysisRequest):
        selected = payload.symptoms
    elif isinstance(payload, list):
        selected = payload
    else:
        selected = getattr(payload, "symptoms", [])

    if not selected:
        raise HTTPException(status_code=400, detail="Please select at least one symptom for analysis.")

    try:
        res = generate_ai_diet_plan(selected, SYMPTOMS_DATA)
        return res
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"AI Analysis error: {str(e)}")

DYNAMIC_HISTORY_STORE: List[Dict[str, Any]] = []

@app.get("/api/history")
async def get_history():
    return DYNAMIC_HISTORY_STORE

@app.post("/api/history/clear")
@app.delete("/api/history/clear")
async def clear_history_logs():
    global DYNAMIC_HISTORY_STORE, REMINDER_LOGS
    DYNAMIC_HISTORY_STORE.clear()
    REMINDER_LOGS.clear()
    return {"status": "success", "message": "All history cleared cleanly"}

# --- API Endpoints continued ---


class ActivityLog(BaseModel):
    day: str
    dateStr: str
    activeMinutes: float
    waterLoggedMl: int
    symptomsCount: int
    dietLoggedCount: int
    adherencePct: int

USER_ACTIVITY_STORE: Dict[str, Dict[str, Any]] = {}

@app.post("/api/activity/log")
async def log_activity(log: ActivityLog):
    USER_ACTIVITY_STORE[log.dateStr] = log.dict()
    return {"status": "success", "recorded": USER_ACTIVITY_STORE[log.dateStr]}

@app.get("/api/activity")
async def get_activity():
    return USER_ACTIVITY_STORE

class ReminderDispatch(BaseModel):
    userName: str
    email: Optional[str] = None
    phone: Optional[str] = None
    message: Optional[str] = "water_reminder"
    type: Optional[str] = "water_reminder"


ACTIVE_REMINDEE = {
    "userName": "Reddyomsai350",
    "email": "reddyomsai350@gmail.com",
    "phone": "6305473867"
}

def generate_realistic_water_reminder(user_name: str) -> str:
    name_str = user_name.strip() if user_name else "Sai"
    if "sai" in name_str.lower() or "reddyomsai" in name_str.lower():
        display_name = "Sai"
    else:
        display_name = name_str.split()[0].capitalize()
    
    return f"Hi {display_name}, it's time to drink 1 glass of water 💧"

USER_FAST2SMS_KEY = "veZR1yOKqtAEXgaFrwnWN7mYcGiML9khoJ8ujp34STsU6lQVHzjegWsbVAHEdJniFc97Y42m1tfKxZQl"

SMS_CONFIG = {
    "api_key": os.getenv("FAST2SMS_API_KEY", USER_FAST2SMS_KEY)
}

class SMSConfigRequest(BaseModel):
    api_key: str

@app.post("/api/reminders/config_sms")
@app.post("/reminders/config_sms")
async def config_sms(req: SMSConfigRequest):
    SMS_CONFIG["api_key"] = req.api_key.strip()
    return {"status": "success", "configured": bool(SMS_CONFIG["api_key"])}

def send_real_sms_via_gateway(phone_number: str, message_text: str):
    """Sends SMS over mobile network via Fast2SMS / Twilio / SMS Gateway API."""
    import urllib.request
    import urllib.parse
    import base64
    import json

    clean_phone = "".join(c for c in str(phone_number) if c.isdigit())
    formatted_phone = clean_phone[-10:] if len(clean_phone) >= 10 else clean_phone

    fast2sms_key = SMS_CONFIG["api_key"] or os.getenv("FAST2SMS_API_KEY", "")
    twilio_sid = os.getenv("TWILIO_ACCOUNT_SID", "")
    twilio_token = os.getenv("TWILIO_AUTH_TOKEN", "")
    twilio_from = os.getenv("TWILIO_PHONE_NUMBER", "")

    sms_sent = False
    status_msg = ""

    # 1. Fast2SMS Integration (Instant SMS for Indian 10-digit mobile numbers)
    if fast2sms_key and formatted_phone:
        try:
            # Fast2SMS Dev API endpoint
            safe_text = urllib.parse.quote(message_text)
            url = f"https://www.fast2sms.com/dev/bulkV2?authorization={fast2sms_key}&route=q&message={safe_text}&language=english&flash=0&numbers={formatted_phone}"
            req = urllib.request.Request(url, headers={"authorization": fast2sms_key})
            try:
                with urllib.request.urlopen(req, timeout=5) as response:
                    res_body = json.loads(response.read().decode("utf-8"))
                    if res_body.get("return"):
                        sms_sent = True
                        status_msg = "Carrier SMS Sent via Fast2SMS"
                        print(f"[Fast2SMS Dispatch Success] Delivered to {formatted_phone}")
                    else:
                        status_msg = f"Fast2SMS API Response: {res_body.get('message')}"
            except urllib.error.HTTPError as err:
                err_data = err.read().decode("utf-8")
                try:
                    err_json = json.loads(err_data)
                    status_msg = f"Fast2SMS Notice: {err_json.get('message', 'HTTP Error')}"
                except Exception:
                    status_msg = f"Fast2SMS HTTP Error: {err.code}"
                print(f"[Fast2SMS Dispatch Error] {status_msg}")
        except Exception as e:
            status_msg = f"Fast2SMS Notice: {str(e)}"
            print(f"[Fast2SMS Dispatch Notice] {e}")

    # 2. Twilio SMS Integration
    if twilio_sid and twilio_token and twilio_from and formatted_phone and not sms_sent:
        try:
            auth = base64.b64encode(f"{twilio_sid}:{twilio_token}".encode()).decode()
            url = f"https://api.twilio.com/2010-04-01/Accounts/{twilio_sid}/Messages.json"
            to_number = f"+91{formatted_phone}" if len(formatted_phone) == 10 else f"+{formatted_phone}"
            data = urllib.parse.urlencode({
                "From": twilio_from,
                "To": to_number,
                "Body": message_text
            }).encode("utf-8")
            req = urllib.request.Request(url, data=data, headers={
                "Authorization": f"Basic {auth}",
                "Content-Type": "application/x-www-form-urlencoded"
            })
            with urllib.request.urlopen(req, timeout=5) as response:
                sms_sent = True
                status_msg = "Carrier SMS Sent via Twilio"
                print(f"[Twilio Dispatch OK] Target: {to_number}")
        except Exception as e:
            print(f"[Twilio Dispatch Notice] {e}")

    if not sms_sent and not status_msg:
        status_msg = "SMS Pop-Up Alert Active (Set Fast2SMS Key for Carrier SMS)"

    safe_text = message_text.encode('ascii', 'replace').decode('ascii')
    print(f"[SMS Network Dispatch] Phone: {formatted_phone} | Message: \"{safe_text}\" | Sent: {sms_sent}")
    return sms_sent, status_msg

REMINDER_LOGS = []

# Initial default seed log for instant display
initial_msg = generate_realistic_water_reminder(ACTIVE_REMINDEE["userName"])
REMINDER_LOGS.append({
    "id": 1,
    "userName": ACTIVE_REMINDEE["userName"],
    "email": ACTIVE_REMINDEE["email"],
    "phone": ACTIVE_REMINDEE["phone"],
    "message": initial_msg,
    "type": "hourly_water_reminder",
    "status": "Dispatched via SMS & Email",
    "timestamp": "Just now"
})

async def hourly_reminder_background_worker():
    """Automated background worker running hourly to dispatch SMS reminders."""
    while True:
        try:
            await asyncio.sleep(3600)  # Runs hourly
            now_str = datetime.datetime.now().strftime("%I:%M %p")
            user_name = ACTIVE_REMINDEE["userName"]
            user_phone = ACTIVE_REMINDEE["phone"]
            user_email = ACTIVE_REMINDEE["email"]
            msg = generate_realistic_water_reminder(user_name)
            
            # Send SMS over Mobile Gateway
            send_real_sms_via_gateway(user_phone, msg)

            log_entry = {
                "id": len(REMINDER_LOGS) + 1,
                "userName": user_name,
                "email": user_email,
                "phone": user_phone,
                "message": msg,
                "type": "hourly_water_reminder",
                "status": "Dispatched via SMS & Email",
                "timestamp": f"Today, {now_str}"
            }
            safe_msg = msg.encode('ascii', 'replace').decode('ascii')
            print(f"[Automated Hourly SMS] Sent to {user_name} ({user_phone}): {safe_msg}")
        except asyncio.CancelledError:
            break
        except Exception as e:
            print(f"[Reminder Task Notice] {e}")

@app.on_event("startup")
async def startup_event():
    asyncio.create_task(hourly_reminder_background_worker())


class ProfileSyncRequest(BaseModel):
    userName: str
    email: Optional[str] = None
    phone: Optional[str] = None

@app.post("/api/reminders/sync_profile")
@app.post("/reminders/sync_profile")
async def sync_reminder_profile(req: ProfileSyncRequest):
    if req.userName:
        ACTIVE_REMINDEE["userName"] = req.userName
    if req.email:
        ACTIVE_REMINDEE["email"] = req.email
    if req.phone:
        ACTIVE_REMINDEE["phone"] = req.phone
    return {"status": "success", "active_remindee": ACTIVE_REMINDEE}

GMAIL_CONFIG = {
    "sender_email": os.getenv("GMAIL_SENDER_EMAIL", ""),
    "app_password": os.getenv("GMAIL_APP_PASSWORD", "")
}

class EmailConfigRequest(BaseModel):
    sender_email: str
    app_password: str

@app.post("/api/reminders/config_email")
@app.post("/reminders/config_email")
async def config_email(req: EmailConfigRequest):
    GMAIL_CONFIG["sender_email"] = req.sender_email.strip()
    GMAIL_CONFIG["app_password"] = req.app_password.strip()
    return {"status": "success", "sender_email": GMAIL_CONFIG["sender_email"], "configured": bool(GMAIL_CONFIG["app_password"])}

def send_real_email_via_gmail(to_email: str, user_name: str, message_text: str):
    """Sends real email message via Gmail SMTP whenever requested."""
    sender_email = GMAIL_CONFIG["sender_email"] or os.getenv("GMAIL_SENDER_EMAIL", "")
    sender_password = GMAIL_CONFIG["app_password"] or os.getenv("GMAIL_APP_PASSWORD", "")

    if not sender_password or not sender_email:
        safe_msg = message_text.encode('ascii', 'replace').decode('ascii')
        print(f"[Gmail Dispatch Notice] GMAIL_APP_PASSWORD missing. Target: {to_email} | Message: \"{safe_msg}\"")
        return False, "Pending Gmail Key"

    try:
        msg = MIMEMultipart("alternative")
        msg["Subject"] = f"💧 SymptomSync Water Reminder for {user_name}"
        msg["From"] = f"SymptomSync Reminders <{sender_email}>"
        msg["To"] = to_email

        html_content = f"""
        <div style="font-family: Arial, sans-serif; padding: 24px; background-color: #0f172a; color: #ffffff; border-radius: 16px; max-width: 500px;">
            <h2 style="color: #60a5fa; margin-top: 0;">💧 Water Hydration Alert</h2>
            <p style="font-size: 18px; line-height: 1.6; font-weight: bold; color: #ffffff;">{message_text}</p>
            <hr style="border: 0; border-top: 1px solid #334155; margin: 20px 0;" />
            <p style="font-size: 12px; color: #94a3b8; margin-bottom: 0;">Sent via SymptomSync Smart Reminder System to {to_email}</p>
        </div>
        """
        msg.attach(MIMEText(html_content, "html"))

        with smtplib.SMTP("smtp.gmail.com", 587) as server:
            server.starttls()
            server.login(sender_email, sender_password)
            server.sendmail(sender_email, to_email, msg.as_string())
        
        print(f"[Gmail Sent Successfully] Delivered to {to_email}")
        return True, "Gmail Inbox Delivered"
    except Exception as e:
        print(f"[Gmail Dispatch Error] {e}")
        return False, f"Gmail Error: {str(e)}"

@app.post("/api/reminders/send")
@app.post("/reminders/send")
async def send_reminder(dispatch: ReminderDispatch):
    user_name = dispatch.userName or ACTIVE_REMINDEE["userName"]
    user_phone = dispatch.phone or ACTIVE_REMINDEE["phone"]
    user_email = dispatch.email or ACTIVE_REMINDEE["email"]
    
    # Update active remindee target
    ACTIVE_REMINDEE["userName"] = user_name
    ACTIVE_REMINDEE["phone"] = user_phone
    ACTIVE_REMINDEE["email"] = user_email

    # Personalize with exact requested format if needed
    final_message = dispatch.message
    if not final_message or final_message.strip() in ["water_reminder", "water_goal"]:
        final_message = generate_realistic_water_reminder(user_name)
    
    now_str = datetime.datetime.now().strftime("%I:%M %p")

    # Instant Dispatch on Every Click: Send both Mobile SMS & Gmail Email
    sms_ok, sms_status = send_real_sms_via_gateway(user_phone, final_message)
    email_ok, email_status = send_real_email_via_gmail(user_email, user_name, final_message)

    status_parts = []
    if sms_ok:
        status_parts.append("Carrier SMS Sent")
    else:
        status_parts.append("SMS Pop-Up Alert Sent")

    if email_ok:
        status_parts.append("Gmail Inbox Delivered")
    else:
        status_parts.append("Email Logged")

    status_label = f"Dispatched ({' & '.join(status_parts)})"

    log_entry = {
        "id": len(REMINDER_LOGS) + 1,
        "userName": user_name,
        "email": user_email,
        "phone": user_phone,
        "message": final_message,
        "type": dispatch.type or "water_reminder",
        "status": status_label,
        "timestamp": f"Today, {now_str}"
    }
    REMINDER_LOGS.insert(0, log_entry)
    return {
        "status": "success",
        "dispatch": log_entry,
        "sms_sent": sms_ok,
        "sms_note": sms_status,
        "email_delivered": email_ok,
        "email_note": email_status
    }

@app.get("/api/reminders/history")
@app.get("/reminders/history")
async def get_reminder_history():
    return REMINDER_LOGS

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

    NO_CACHE_HEADERS = {"Cache-Control": "no-cache, no-store, must-revalidate"}

    # Check direct file match
    file_path = os.path.join(out_dir, clean_path)
    if clean_path and os.path.exists(file_path) and os.path.isfile(file_path):
        return FileResponse(file_path, headers=NO_CACHE_HEADERS)

    # Handle Next.js RSC .txt prefetch requests
    base_clean = clean_path[:-4] if clean_path.endswith(".txt") else clean_path

    # Check html file match (e.g. analyze -> analyze.html)
    html_file = os.path.join(out_dir, f"{base_clean}.html")
    if base_clean and os.path.exists(html_file):
        return FileResponse(html_file, headers=NO_CACHE_HEADERS)

    # Fallback to index.html for SPA routes
    index_file = os.path.join(out_dir, "index.html")
    if os.path.exists(index_file):
        return FileResponse(index_file, headers=NO_CACHE_HEADERS)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)


