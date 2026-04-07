#!/bin/bash
# Compile script for AgriEdge OMS CLI application

echo "Compiling AgriEdge OMS..."
echo

cd "$(dirname "$0")"

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
javac -d bin src/com/agriedge/*.java

if [ $? -eq 0 ]; then
    echo
    echo "==================================================="
    echo "Compilation successful!"
    echo "Class files are in: bin/com/agriedge/"
    echo
    echo "To run the application, use: ./run.sh"
    echo "==================================================="
else
    echo
    echo "==================================================="
    echo "Compilation failed!"
    echo "==================================================="
    exit 1
fi
