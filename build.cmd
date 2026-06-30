@echo off
setlocal
chcp 65001 > nul

set "JAVA_HOME=%USERPROFILE%\main\pleiades\java\17"

call gradlew.bat jar

endlocal
