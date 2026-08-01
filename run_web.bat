@echo off
title SymptomSync Local Server
echo ==================================================
echo   SymptomSync - Starting Unified App & API Server
echo ==================================================
echo.
echo Launching full application on http://localhost:8000...
echo.

start http://localhost:8000

cd backend
python main.py

pause
