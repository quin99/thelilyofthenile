#FROM openjdk:17-jdk-alpine
#VOLUME /tmp
#COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM mcr.microsoft.com/devcontainers/java:17

# Install Node.js + npm
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs

# Install Angular CLI
RUN npm install -g @angular/cli

# Set work directory
WORKDIR /workspace
