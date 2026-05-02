# We use the full Maven image so it can compile on the fly
FROM maven:3.9-eclipse-temurin-17

# Set our working directory
WORKDIR /app

# This tells Docker to just run the compiled JAR directly, ignoring Maven!
CMD sh -c 'java -jar target/*.jar'
