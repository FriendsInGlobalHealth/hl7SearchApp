# Set the base image to use
FROM openjdk:8

# Set the working directory
WORKDIR /opt/app

# Copy the JAR file to the container
COPY target/hl7SearchApp-0.0.1-SNAPSHOT.jar /opt/app

# Expose the port that your application is running on
EXPOSE 9191

# Start the application
CMD ["java", "-jar", "hl7SearchApp-0.0.1-SNAPSHOT.jar"]
