# Gradle with JRE (Java) 11
FROM gradle:5.2.1-jre11-slim

# App Directory
ENV APPDIR /home/gradle/project

WORKDIR $APPDIR

# Make the user 'gradle' the owner of the app directory
ADD --chown=gradle . $APPDIR

# All relevant files required for app
COPY ./core/src ${APPDIR}/core/src
COPY ./core/lib ${APPDIR}/core/lib
COPY ./core/build.gradle ${APPDIR}/core/build.gradle
COPY ./core/settings.gradle ${APPDIR}/core/settings.gradle
COPY ./api-sparkjava/src ${APPDIR}/api-sparkjava/src
COPY ./api-sparkjava/build.gradle ${APPDIR}/api-sparkjava/build.gradle
COPY ./api-sparkjava/settings.gradle ${APPDIR}/api-sparkjava/settings.gradle
COPY ./build.gradle ${APPDIR}/build.gradle
COPY ./settings.gradle ${APPDIR}/settings.gradle

# Make port 4567 available to the world outside this container
EXPOSE 4567

# Use Gradle to build and run the App
CMD ["gradle", "clean", "build", "run"]