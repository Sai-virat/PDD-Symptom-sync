@echo off
setlocal enabledelayedexpansion
echo ==================================================
echo   SymptomSync - Web Application Runner (Updated)
echo ==================================================
echo.

REM 1. Try to find Android Studio's bundled JDK
set "STUDIO_JDK=C:\Program Files\Android\Android Studio\jbr"
if exist "%STUDIO_JDK%\bin\java.exe" (
    echo [OK] Found Android Studio JDK at: %STUDIO_JDK%
    set "JAVA_HOME=%STUDIO_JDK%"
    set "PATH=%STUDIO_JDK%\bin;%PATH%"
) else (
    echo [!] Searching for Android Studio JDK in other locations...
    for /d %%i in ("C:\Program Files\Android\Android Studio*") do (
        if exist "%%i\jbr\bin\java.exe" (
            set "STUDIO_JDK=%%i\jbr"
            echo [OK] Found JDK at: !STUDIO_JDK!
            set "JAVA_HOME=!STUDIO_JDK!"
            set "PATH=!STUDIO_JDK!\bin;!PATH!"
        )
    )
)

REM 2. Verify Java
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [!!] Java is not recognized. Please make sure Android Studio is installed correctly.
    pause
    exit /b 1
)

echo.
echo [1/2] Starting the Web App (Production Mode)...
echo.

REM Attempt to run using the specific production task
call .\gradlew.bat :composeApp:wasmJsBrowserProductionRun --no-configuration-cache
if %ERRORLEVEL% EQU 0 goto end

echo.
echo [!] Gradle run failed. Trying to serve pre-built files instead...
echo.

REM 3. Build Distribution if needed
if not exist "composeApp\build\dist\wasmJs\productionExecutable\index.html" (
    echo [Building] Generating web files...
    call .\gradlew.bat :composeApp:wasmJsBrowserDistribution --no-configuration-cache
)

echo.
echo [2/2] Attempting to start a local server...
echo.

REM Check for Python
python --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [OK] Detected Python. Starting server at http://localhost:8080
    start http://localhost:8080
    python -m http.server 8080 --directory "composeApp\build\dist\wasmJs\productionExecutable\"
    goto end
)

REM Check for Node
npx --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [OK] Detected Node.js. Starting server at http://localhost:8080
    npx serve "composeApp\build\dist\wasmJs\productionExecutable\"
    goto end
)

echo.
echo [DONE] Web files are ready at:
echo C:\Users\ASUS\AndroidStudioProjects\Symptomsync3\composeApp\build\dist\wasmJs\productionExecutable\
echo.
echo Note: Please install Python or Node.js to view the app automatically,
echo or use the files above.
echo.
pause

:end
echo.
echo Done.
pause
