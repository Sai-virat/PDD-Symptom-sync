import os
import shutil

FRONTEND_OUT = os.path.join(os.path.dirname(__file__), "..", "frontend", "out")
ANDROID_APP_ASSETS = os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "assets")
COMPOSE_APP_ASSETS = os.path.join(os.path.dirname(__file__), "..", "composeApp", "src", "androidMain", "assets")

def sync_assets():
    if os.path.exists(FRONTEND_OUT):
        for target in [ANDROID_APP_ASSETS, COMPOSE_APP_ASSETS]:
            if os.path.exists(target):
                shutil.rmtree(target)
            shutil.copytree(FRONTEND_OUT, target)
            print(f"[OK] Synced static web app bundle to: {target}")
    else:
        print("[Warning] frontend/out does not exist. Run npm run build first.")

if __name__ == "__main__":
    sync_assets()
