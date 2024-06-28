#!/bin/bash

# Clone the repository if not already cloned
if [ ! -d "spring-petclinic" ]; then
  git submodule update --init --recursive
fi

# Generate master key if it doesn't exist
if [ ! -f "artifactory/master.key" ]; then
  openssl rand -hex 16 > artifactory/master.key
fi

# Start the Docker containers
docker-compose build
docker-compose up -d
