# JRE (Java) 11.0.2
FROM openjdk:11.0.2-jre-slim

LABEL name = "JMS Social Media"
LABEL description = "Simple Social Media Web Site Written AngularJS and Java"

# Should contain all jars
ARG PATH_TO_LIB

# Get bash to run wait-for-it.sh
RUN apt-get update && apt-get install bash

# App Directory
ENV APP_DIR /opt/app

WORKDIR $APP_DIR

# All relevant jars required for App
COPY ${PATH_TO_LIB} lib

# Copy 2 Scripts needed to run the App
# wait-for-it.sh helps us wait for MySQL before starting
COPY docker_scripts/wait-for-it.sh .
COPY docker_scripts/start.sh .

# Allow them to be executed
RUN chmod +x wait-for-it.sh
RUN chmod +x start.sh

# Make port 4567 available to the world outside this container
EXPOSE 4567

# Run the App with all dependencies using the Start Script
CMD ["./start.sh"]