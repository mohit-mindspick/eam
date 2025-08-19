#!/bin/bash

echo "Starting EAM in development mode with H2 database..."
echo "This includes enhanced logging and debugging information."
echo "H2 Console will be available at: http://localhost:8080/h2-console"
echo ""

./gradlew bootRun --args='--spring.profiles.active=dev'
