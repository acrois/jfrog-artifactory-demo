FROM jenkins/jenkins:lts

# Install Maven
USER root
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Ensure Jenkins runs as the Jenkins user
USER jenkins

# Create directory for plugins
RUN mkdir -p /usr/share/jenkins/ref/plugins

# Copy the Groovy init script to the init.groovy.d directory
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
