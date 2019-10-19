#!/bin/bash

## Login to Docker Registries
echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
echo $GITHUB_TOKEN | docker login -u JasonSarwar --password-stdin

## Make Jars for Docker Build
mvn -P assemble-jars package -B

## For Docker Image Tags
DOCKERTAG="${TRAVIS_TAG:1}-${TRAVIS_COMMIT:0:7}"
SEMVER=( ${DOCKERTAG//./ } )
MAJOR="${SEMVER[0]}"
MAJOR_MINOR="${MAJOR}.${SEMVER[1]}"
MAJOR_MINOR_PATCH="${MAJOR_MINOR}.${SEMVER[2]}"

## Docker Repositories
DOCKER_REPO=${DOCKER_USERNAME}/jms-social-media
GITHUB_DOCKER_REPO="docker.pkg.github.com/jasonsarwar/jms-social-media/app"

## Build the Docker Image
docker build --build-arg PATH_TO_LIB=./api/target/libs/ -t $DOCKER_REPO --label "commit=$TRAVIS_COMMIT" --label "version=${TRAVIS_TAG:1}" --label "tag=$DOCKERTAG" .

## Tag the Docker Image with multiple tags
docker tag ${DOCKER_REPO} ${DOCKER_REPO}:${MAJOR}
docker tag ${DOCKER_REPO} ${DOCKER_REPO}:${MAJOR_MINOR}
docker tag ${DOCKER_REPO} ${DOCKER_REPO}:${MAJOR_MINOR_PATCH}
docker tag ${DOCKER_REPO} ${DOCKER_REPO}:${DOCKERTAG}
docker tag ${DOCKER_REPO} ${GITHUB_DOCKER_REPO}:${MAJOR_MINOR_PATCH}
docker tag ${DOCKER_REPO} ${GITHUB_DOCKER_REPO}:${DOCKERTAG}

## Push the Docker Images
docker push ${DOCKER_REPO}:${MAJOR}
docker push ${DOCKER_REPO}:${MAJOR_MINOR}
docker push ${DOCKER_REPO}:${MAJOR_MINOR_PATCH}
docker push ${DOCKER_REPO}:${DOCKERTAG}
docker push ${DOCKER_REPO}:latest
docker push ${GITHUB_DOCKER_REPO}:${MAJOR_MINOR_PATCH}
docker push ${GITHUB_DOCKER_REPO}:${DOCKERTAG}
