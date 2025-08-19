#!/bin/bash

echo "Starting EAM with H2 database..."
echo "H2 Console will be available at: http://localhost:8080/h2-console"
echo "JDBC URL: jdbc:h2:mem:testdb"
echo "Username: sa"
echo "Password: password"
echo ""

./gradlew bootRun --args='--spring.profiles.active=h2'
