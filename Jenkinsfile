pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'spring-petclinic'
        ARTIFACTORY_INSTANCE_ID = 'artifactory'
        ARTIFACTORY_REPO_MAVEN = 'petclinic-mvn'
        ARTIFACTORY_CRED_ID = 'artifactory-credentials'
        SPRING_PETCLINIC_DIR = '/var/jenkins_home/petclinic-demo'
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    // Check if the local directory exists
                    if (fileExists(env.SPRING_PETCLINIC_DIR)) {
                        echo 'Local spring-petclinic directory exists, using local directory.'
                        env.LOCAL_DIR = "${env.SPRING_PETCLINIC_DIR}/spring-petclinic"
                        env.SKIP_CHECKOUT = 'true'
                        sh "git config --global --add safe.directory ${env.SPRING_PETCLINIC_DIR}/.git/modules/spring-petclinic"
                    } else {
                        echo 'Local spring-petclinic directory does not exist, performing checkout.'
                        env.LOCAL_DIR = 'spring-petclinic'
                        env.SKIP_CHECKOUT = 'false'
                    }
                }
            }
        }
        
        stage('Checkout') {
            when {
                expression { env.SKIP_CHECKOUT == 'false' }
            }
            steps {
                // Checkout main repository and initialize submodules
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    userRemoteConfigs: [[url: 'https://github.com/acrois/jfrog-artifactory-demo']],
                    extensions: [[$class: 'SubmoduleOption', recursiveSubmodules: true]]
                ])
            }
        }

        stage('Compile') {
            steps {
                dir(env.LOCAL_DIR) {
                    sh 'mvn compile --settings ../settings.xml'
                }
            }
        }
        
        stage('Test') {
            steps {
                dir(env.LOCAL_DIR) {
                    sh 'mvn test --settings ../settings.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                dir(env.LOCAL_DIR) {
                    sh 'mvn package --settings ../settings.xml'
                }
            }
        }
        
        stage('Upload JAR to Artifactory') {
            steps {
                script {
                    dir(env.LOCAL_DIR) {
                        def server = Artifactory.server(env.ARTIFACTORY_INSTANCE_ID)
                        def uploadSpec = """{
                            "files": [
                                {
                                    "pattern": "target/*.jar",
                                    "target": "${ARTIFACTORY_REPO_MAVEN}/"
                                }
                            ]
                        }"""
                        server.upload spec: uploadSpec, buildInfo: Artifactory.newBuildInfo()
                    }
                }
            }
        }

        // stage('Download Artifact from Artifactory') {
        //     steps {
        //         script {
        //             dir(env.LOCAL_DIR) {
        //                 withCredentials([usernamePassword(credentialsId: env.ARTIFACTORY_CRED_ID, usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
        //                     sh """
        //                     curl -u ${ARTIFACTORY_USER}:${ARTIFACTORY_PASSWORD} \
        //                     -O ${ARTIFACTORY_URL}/${ARTIFACTORY_REPO_MAVEN}/app.jar
        //                     """
        //                     sh 'mv app.jar ../'
        //                 }
        //             }
        //         }
        //     }
        // }

        stage('Download Artifact from Artifactory') {
            steps {
                script {
                    dir(env.LOCAL_DIR) {
                        def server = Artifactory.server(env.ARTIFACTORY_INSTANCE_ID)
                        def downloadSpec = """{
                            "files": [
                                {
                                    "pattern": "${env.ARTIFACTORY_REPO_MAVEN}/*.jar",
                                    "target": "../app.jar",
                                    "flat": "true"
                                }
                            ]
                        }"""
                        server.download spec: downloadSpec, buildInfo: Artifactory.newBuildInfo()
                    }
                }
            }
        }

        // stage('Build Docker Image') {
        //     steps {
        //         dir(env.LOCAL_DIR) {
        //             sh 'mvn spring-boot:build-image'
        //         }
        //     }
        // }
        
        stage('Build Docker Image') {
            steps {
                script {
                    dir(env.LOCAL_DIR) {
                        sh "docker build -t ${DOCKER_IMAGE} ../"
                    }
                }
            }
        }
        
        // stage('Push Docker Image') {
        //     steps {
        //         script {
        //             docker.withRegistry("${ARTIFACTORY_URL}/${ARTIFACTORY_REPO_DOCKER}", "${ARTIFACTORY_CRED_ID}") {
        //                 sh "docker push ${DOCKER_IMAGE}"
        //             }
        //         }
        //     }
        // }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}