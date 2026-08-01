import os
import shutil

FRONTEND_OUT = os.path.join(os.path.dirname(__file__), "..", "frontend", "out")
ANDROID_ASSETS = os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "assets")

def sync_assets():
    if os.path.exists(FRONTEND_OUT):
        if os.path.exists(ANDROID_ASSETS):
            shutil.rmtree(ANDROID_ASSETS)
        shutil.copytree(FRONTEND_OUT, ANDROID_ASSETS)
        print(f"[OK] Synced static web app bundle to Android assets: {ANDROID_ASSETS}")
    else:
        print("[Warning] frontend/out does not exist. Run npm run build first.")

if __name__ == "__main__":
    sync_assets()
