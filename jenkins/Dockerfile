FROM jenkins/jenkins:lts

# Install Maven
USER root
RUN apt-get update \
    && apt-get install -y \
        ca-certificates \
        curl \
        maven \
    && install -m 0755 -d /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc \
    && chmod a+r /etc/apt/keyrings/docker.asc \
    && echo \
        "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
        $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
        tee /etc/apt/sources.list.d/docker.list > /dev/null \
    && apt-get update \
    && apt-get install -y \
        docker-ce \
        docker-ce-cli \
        containerd.io \
        docker-buildx-plugin \
        docker-compose-plugin \
    && apt-get clean

# Ensure Jenkins runs as the Jenkins user
USER jenkins

# Create directory for plugins
RUN mkdir -p /usr/share/jenkins/ref/plugins

# Copy the Groovy init script to the init.groovy.d directory
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
