#!/bin/bash
# Run script for AgriEdge OMS CLI application

cd "$(dirname "$0")"

if [ ! -d bin ]; then
    echo
    echo "==================================================="
    echo "Error: Class files not found!"
    echo "Please run ./compile.sh first."
    echo "==================================================="
    exit 1
fi

echo
echo "==================================================="
echo "Starting AgriEdge OMS..."
echo "==================================================="
echo

java -cp bin com.agriedge.Main

echo
echo "==================================================="
echo "AgriEdge OMS terminated."
echo "==================================================="
