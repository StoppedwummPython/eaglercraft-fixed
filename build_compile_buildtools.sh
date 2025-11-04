#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# --- Get the absolute path of the project root ---
# This ensures all paths are resolved correctly, even when changing directories.
PROJECT_ROOT=$(pwd)

# --- Environment Check ---
# Check if JAVA_HOME is set. If not, print an error and exit.
if [ -z "$JAVA_HOME" ]; then
    echo "Error: JAVA_HOME is not set. Please set it to your JDK installation directory."
    exit 1
fi

echo "Using JDK from JAVA_HOME: $JAVA_HOME"

# --- Configuration ---
BUILD_DIR="build"
CLASSES_DIR="$BUILD_DIR/classes"
SRC_JAVA_DIR="buildtools/src/main/java"
SRC_RESOURCES_DIR="buildtools/src/main/resources"
DEPS_DIR="buildtools/deps"
RESOURCES_DIR="buildtools" # Top-level resources
JAR_NAME="buildtools/BuildTools.jar"
MANIFEST_FILE="$RESOURCES_DIR/MANIFEST.MF"

# Define paths to Java tools using JAVA_HOME
JAVAC="$JAVA_HOME/bin/javac"
JAR="$JAVA_HOME/bin/jar"

# --- Clean and Create Directories ---
echo "Cleaning up previous builds..."
rm -rf "$BUILD_DIR"
rm -rf "$JAR_NAME"
mkdir -p "$CLASSES_DIR"

# --- Compile Java Sources ---
echo "Compiling Java sources..."
find "$SRC_JAVA_DIR" -name "*.java" > sources.txt

# --- Create Classpath (POSIX-compliant method) ---
echo "Building classpath for compilation..."
CP=""
# Loop through all jar files in the deps directory
for jarfile in "$DEPS_DIR"/*.jar; do
  # Check if the file exists before adding it
  if [ -e "$jarfile" ]; then
    # If the classpath is not empty, add a separator first
    if [ -n "$CP" ]; then
      CP="$CP:"
    fi
    # Add the jar file to the classpath
    CP="$CP$jarfile"
  fi
done

# Run the compiler
"$JAVAC" -d "$CLASSES_DIR" -cp "$CP" @sources.txt

# --- Copy Resources ---
echo "Copying resources..."
# Check if the resource directory exists and has content
if [ -d "$SRC_RESOURCES_DIR" ] && [ "$(ls -A $SRC_RESOURCES_DIR)" ]; then
  # The '/.' at the end of the source path ensures the contents of the directory are copied,
  # not the directory itself.
  cp -r "$SRC_RESOURCES_DIR"/. "$CLASSES_DIR"
else
  echo "No resources to copy from $SRC_RESOURCES_DIR."
fi

# --- Unpack Dependencies ---
echo "Unpacking dependencies into build directory..."
for jarfile in "$DEPS_DIR"/*.jar; do
  if [ -e "$jarfile" ]; then
    echo " > Unpacking $jarfile"
    # *** FIX: Construct an absolute path to the jar file to be extracted. ***
    ABSOLUTE_JAR_PATH="$PROJECT_ROOT/$jarfile"
    # Change to the classes directory, unpack the jar using its absolute path, then change back.
    (cd "$CLASSES_DIR" && "$JAR" -xf "$ABSOLUTE_JAR_PATH")
  fi
done


# --- Package ---
echo "Packaging the fat jar..."

# Create the initial JAR file. It now includes your code, resources, and all dependencies.
"$JAR" cfm "$JAR_NAME" "$MANIFEST_FILE" -C "$CLASSES_DIR" .

echo "Adding other top-level files to the JAR..."
# Update the JAR, adding the other necessary files from the buildtools directory
(cd "$RESOURCES_DIR" && "$JAR" uf ../"$JAR_NAME" BuildTools.jar TeaVMBridge.jar Java11Check.jar Java17Check.jar production-favicon.png production-index-ext.html production-index.html MAINFEST-README-PLEASE.txt)


# --- Cleanup ---
rm sources.txt

echo "Build successful! Created fat jar: $JAR_NAME"