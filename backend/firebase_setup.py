import firebase_admin
from firebase_admin import credentials, firestore
import os

# Get the path to the service account key
# Using the specific filename found in the directory
KEY_PATH = os.path.join(os.path.dirname(__file__), "symptomsync-pro-firebase-adminsdk-fbsvc-2c83f2b24b.json")

def initialize_firebase():
    if not firebase_admin._apps:
        cred = credentials.Certificate(KEY_PATH)
        firebase_admin.initialize_app(cred)
    return firestore.client()

db = initialize_firebase()
