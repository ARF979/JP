#!/bin/bash

# Quick Start Script for Google Drive-like Backend

echo "🚀 Starting Google Drive-like Backend..."
echo ""

# Kill any process on port 8080
echo "Checking port 8080..."
lsof -ti:8080 | xargs kill -9 2>/dev/null && echo "Killed existing process on port 8080" || echo "Port 8080 is free"

# Create uploads directory if it doesn't exist
mkdir -p uploads

# Run the application
echo "Starting Spring Boot application..."
./mvnw spring-boot:run

