# JRE (Java) 11.0.3
FROM openjdk:11.0.3-jre-slim

LABEL name = "JMS Social Media"
LABEL description = "Simple Social Media Web Site Written AngularJS and Java"

# Should contain all jars
ARG PATH_TO_LIB

# Make port 4567 available to the world outside this container
EXPOSE 4567

# App Directory
ENV APP_HOME /opt/app/jms

WORKDIR $APP_HOME

# Get bash to run wait-for-it.sh and to mount for properties file and logs
RUN apt-get update \
    && mkdir properties \
    && mkdir logs

# All relevant jars required for App
COPY ${PATH_TO_LIB} lib

# Copy 2 Scripts needed to run the App
# wait-for-it.sh helps us wait for MySQL before starting
COPY docker_scripts/wait-for-it.sh docker_scripts/start.sh ./

# To make the two scripts executable
RUN chmod +x wait-for-it.sh \
    && chmod +x start.sh

# Run the App with all dependencies using the Start Script
CMD ["./start.sh"]