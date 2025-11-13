@echo off
chcp 65001 >nul

title Servidor Blackjack
echo ===============================
echo Iniciando o Servidor Blackjack...
echo ===============================
start cmd /k "chcp 65001 >nul && java -Dfile.encoding=UTF-8 Server"
timeout /t 3 >nul

echo ===============================
echo Iniciando Cliente 1...
echo ===============================
start cmd /k "chcp 65001 >nul && java -Dfile.encoding=UTF-8 Client"
timeout /t 2 >nul

echo ===============================
echo Iniciando Cliente 2...
echo ===============================
start cmd /k "chcp 65001 >nul && java -Dfile.encoding=UTF-8 Client"

echo ===============================
echo Todos os processos foram iniciados!
echo ===============================
pause
