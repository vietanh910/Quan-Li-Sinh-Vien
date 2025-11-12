@echo off
setlocal ENABLEEXTENSIONS

REM Chuyen sang thu muc chua script de tranh sai thu muc lam viec
cd /d "%~dp0"

REM =============== Cấu hình ===============
set "SQLITE_JAR=sqlite-jdbc-3.45.3.0.jar"
set "SQLITE_URL=https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar"
set "SLF4J_API_JAR=slf4j-api-2.0.13.jar"
set "SLF4J_API_URL=https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar"
set "SLF4J_SIMPLE_JAR=slf4j-simple-2.0.13.jar"
set "SLF4J_SIMPLE_URL=https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.13/slf4j-simple-2.0.13.jar"
set "SRC_DIR=src\main\java\com\example\studentapp"
set "OUT_DIR=target\classes"

REM =============== Tự động tải JAR nếu chưa có ===============
call :download "%SQLITE_JAR%" "%SQLITE_URL%"
call :download "%SLF4J_API_JAR%" "%SLF4J_API_URL%"
call :download "%SLF4J_SIMPLE_JAR%" "%SLF4J_SIMPLE_URL%"

REM =============== Tạo thư mục biên dịch nếu chưa có ===============
if not exist "%OUT_DIR%" (
  md "%OUT_DIR%" >nul 2>&1
)

REM =============== Biên dịch mã nguồn ===============
echo Dang bien dich...
javac -encoding UTF-8 -cp "%SQLITE_JAR%;%SLF4J_API_JAR%;%SLF4J_SIMPLE_JAR%" -d "%OUT_DIR%" "%SRC_DIR%\Database.java" "%SRC_DIR%\Student.java" "%SRC_DIR%\StudentDAO.java" "%SRC_DIR%\StudentManagerFrame.java" "%SRC_DIR%\Main.java"
if errorlevel 1 (
  echo Loi bien dich. Kiem tra thong bao o tren.
  pause
  exit /b 1
)

REM =============== Chạy ứng dụng ===============
echo Chay ung dung...
java -cp "%OUT_DIR%;%SQLITE_JAR%;%SLF4J_API_JAR%;%SLF4J_SIMPLE_JAR%" com.example.studentapp.Main
if errorlevel 1 (
  echo Loi khi chay ung dung.
  pause
  exit /b 1
)

pause
exit /b 0

:download
set "JAR_FILE=%~1"
set "JAR_URL=%~2"
if exist "%JAR_FILE%" goto :eof
echo Tai %JAR_FILE% ...
powershell -NoLogo -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%JAR_FILE%' -UseBasicParsing" >nul 2>&1
if not exist "%JAR_FILE%" (
  echo Khong the tai %JAR_FILE% tu Internet: %JAR_URL%
  pause
  exit /b 1
) else (
  echo Da tai xong %JAR_FILE%.
)
exit /b 0
