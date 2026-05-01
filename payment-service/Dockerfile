# We use the full Maven image so it can compile on the fly
FROM maven:3.9-eclipse-temurin-17

# Set our working directory
WORKDIR /app

# I don't copy the code here we'll inject it via Docker Compose.
# --- This command tells the container to boot up Spring Boot directly from the source ---
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'"]
#there's a debugging port uuup here, Remember to expose it in the IDE