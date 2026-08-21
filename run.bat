@echo off
:: Enable UTF-8 encoding in Command Prompt to support box-drawing characters and emojis
chcp 65001 >nul

:: Ensure working directory is the script's directory
cd /d "%~dp0"

:: Compile Java source files
echo Compiling source files...
if not exist out mkdir out
javac -d out src\*.java
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

:: Clear the compilation text and start the program
cls
java -cp out Main
pause
