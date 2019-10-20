#!/bin/bash

## Generate Javadocs
mvn javadoc:aggregate

git clone --branch=gh-pages "https://github.com/${TRAVIS_REPO_SLUG}.git" gh-pages-clone

## Copy Javadocs over to the gh-pages branch
yes | cp -rf target/site/apidocs/ gh-pages-clone/apidocs

cd gh-pages-clone

## Upload Javadocs to gh-pages branch
git config user.name "Travis-CI Deployment Bot"
git config user.email "deploy@travis-ci.org"
git add .
git commit --message "Release ${TRAVIS_TAG}"
git push --quiet "https://${GITHUB_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git" gh-pages
