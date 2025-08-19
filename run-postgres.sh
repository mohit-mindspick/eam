#!/bin/bash

echo "Starting EAM with PostgreSQL database..."
echo "Make sure PostgreSQL is running on localhost:5434"
echo "Database: eam"
echo "Username: postgres"
echo "Password: password"
echo ""

# Check if PostgreSQL container is running
if ! docker ps | grep -q "eam-postgres"; then
    echo "PostgreSQL container is not running. Starting with Docker Compose..."
    docker-compose up -d postgres
    
    echo "Waiting for PostgreSQL to be ready..."
    sleep 10
fi

./gradlew bootRun --args='--spring.profiles.active=postgres'
