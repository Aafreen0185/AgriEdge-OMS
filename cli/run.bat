@echo off
REM Run script for AgriEdge OMS CLI application

cd /d "%~dp0"

if not exist bin (
    echo.
    echo ===================================================
    echo Error: Class files not found!
    echo Please run compile.bat first.
    echo ===================================================
    exit /b 1
)

echo.
echo ===================================================
echo Starting AgriEdge OMS...
echo ===================================================
echo.

java -cp bin com.agriedge.Main

echo.
echo ===================================================
echo AgriEdge OMS terminated.
echo ===================================================
