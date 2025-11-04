@echo off
setlocal

REM Exit immediately if a command exits with a non-zero status.
set "errorlevel="

REM --- Get the absolute path of the project root ---
REM This ensures all paths are resolved correctly, even when changing directories.
set "PROJECT_ROOT=%cd%"

REM --- Environment Check ---
REM Check if JAVA_HOME is set. If not, print an error and exit.
if not defined JAVA_HOME (
    echo Error: JAVA_HOME is not set. Please set it to your JDK installation directory.
    exit /b 1
)

echo Using JDK from JAVA_HOME: %JAVA_HOME%

REM --- Configuration ---
set "BUILD_DIR=build"
set "CLASSES_DIR=%BUILD_DIR%\classes"
set "SRC_JAVA_DIR=buildtools\src\main\java"
set "SRC_RESOURCES_DIR=buildtools\src\main\resources"
set "DEPS_DIR=buildtools\deps"
set "RESOURCES_DIR=buildtools"
set "JAR_NAME=buildtools\BuildTools.jar"
set "MANIFEST_FILE=%RESOURCES_DIR%\MANIFEST.MF"

REM Define paths to Java tools using JAVA_HOME
set "JAVAC=%JAVA_HOME%\bin\javac.exe"
set "JAR=%JAVA_HOME%\bin\jar.exe"

REM --- Clean and Create Directories ---
echo Cleaning up previous builds...
if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
if exist "%JAR_NAME%" del "%JAR_NAME%"
mkdir "%CLASSES_DIR%"

REM --- Compile Java Sources ---
echo Compiling Java sources...
dir /s /b "%SRC_JAVA_DIR%\*.java" > sources.txt

REM --- Create Classpath (Windows-compliant method) ---
echo Building classpath for compilation...
set "CP="
for %%j in ("%DEPS_DIR%\*.jar") do (
    if defined CP (
        set "CP=!CP!;%%~j"
    ) else (
        set "CP=%%~j"
    )
)

REM Run the compiler
"%JAVAC%" -d "%CLASSES_DIR%" -cp "%CP%" @sources.txt

REM --- Copy Resources ---
echo Copying resources...
if exist "%SRC_RESOURCES_DIR%" (
    xcopy "%SRC_RESOURCES_DIR%" "%CLASSES_DIR%" /e /i /h /y
) else (
    echo No resources to copy from %SRC_RESOURCES_DIR%.
)

REM --- Unpack Dependencies ---
echo Unpacking dependencies into build directory...
for %%j in ("%DEPS_DIR%\*.jar") do (
    echo  > Unpacking %%~nxj
    pushd "%CLASSES_DIR%"
    "%JAR%" -xf "%PROJECT_ROOT%\%%~j"
    popd
)

REM --- Package ---
echo Packaging the fat jar...

REM Create the initial JAR file.
"%JAR%" cfm "%JAR_NAME%" "%MANIFEST_FILE%" -C "%CLASSES_DIR%" .

echo Adding other top-level files to the JAR...
pushd "%RESOURCES_DIR%"
"%JAR%" uf ../"%JAR_NAME%" BuildTools.jar TeaVMBridge.jar Java11Check.jar Java17Check.jar production-favicon.png production-index-ext.html production-index.html MAINFEST-README-PLEASE.txt
popd

REM --- Cleanup ---
del sources.txt

echo Build successful! Created fat jar: %JAR_NAME%