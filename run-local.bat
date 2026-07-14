@echo off
setlocal

cd /d "%~dp0"

if not exist "gradlew.bat" (
    echo gradlew.bat not found. Please run this file from the project directory.
    pause
    exit /b 1
)

echo Starting Roadpia AAS Server with maintech,local profiles...
echo Swagger: http://localhost:8090/swagger-ui.html
echo Press Ctrl+C to stop the server.
echo.

call gradlew.bat bootRun --args="--spring.profiles.active=maintech,local"

echo.
echo Server process ended.
pause
