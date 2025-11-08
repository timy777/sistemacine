@echo off
echo ==========================================
echo Cerrando procesos que usan el puerto 8082
echo ==========================================

FOR /F "tokens=5" %%P IN ('netstat -ano ^| findstr :8082 ^| findstr LISTENING') DO (
    echo Matando proceso con PID %%P ...
    taskkill /PID %%P /F >nul 2>&1
)

echo ------------------------------------------
echo Listo! El puerto 8082 esta libre.
pause
