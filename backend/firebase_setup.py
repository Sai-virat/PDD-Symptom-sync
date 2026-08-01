import firebase_admin
from firebase_admin import credentials, firestore
import os

KEY_PATH = os.path.join(os.path.dirname(__file__), "symptomsync-pro-firebase-adminsdk-fbsvc-2c83f2b24b.json")

def initialize_firebase():
    try:
        if not firebase_admin._apps:
            if os.path.exists(KEY_PATH):
                cred = credentials.Certificate(KEY_PATH)
                firebase_admin.initialize_app(cred)
            else:
                return None
        return firestore.client()
    except Exception as e:
        print(f"[Warning] Firebase initialization failed: {e}. Falling back to in-memory store.")
        return None

db = initialize_firebase()

