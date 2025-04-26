#!/bin/bash

echo "Compiling..."

# Create output directory
mkdir -p out

# Find all Java source files under main/src
find main/src -name "*.java" > sources.txt

# Compile Java files, putting .class files in the out folder
javac -cp "lib/*" -d out @sources.txt

echo "Running your app..."

# Run your Main class from out directory
java -cp "out:lib/*" Main
