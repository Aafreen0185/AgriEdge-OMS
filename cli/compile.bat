@echo off
REM Compile script for AgriEdge OMS CLI application

echo Compiling AgriEdge OMS...
echo.

cd /d "%~dp0"

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files
javac -d bin src\com\agriedge\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===================================================
    echo Compilation successful!
    echo Class files are in: bin\com\agriedge\
    echo.
    echo To run the application, use: run.bat
    echo ===================================================
) else (
    echo.
    echo ===================================================
    echo Compilation failed!
    echo ===================================================
    exit /b 1
)
