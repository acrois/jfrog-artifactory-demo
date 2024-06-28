#!groovy

import jenkins.model.*
import hudson.security.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
// import org.jfrog.hudson.*

def instance = Jenkins.getInstance()

// Configure security
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin', 'admin')
instance.setSecurityRealm(hudsonRealm)
instance.save()

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)
instance.save()

// Add Artifactory credentials
// def domain = Domain.global()
// def credentialsStore = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

// def usernameAndPassword = new UsernamePasswordCredentialsImpl(
//   CredentialsScope.GLOBAL,
//   "artifactory-credentials", // ID
//   "Artifactory credentials", // Description
//   "jenkins", // Username
//   "Jinkies123!" // Password
// )

// credentialsStore.addCredentials(domain, usernameAndPassword)

// // Configure Artifactory server
// def deployerCredentialsConfig = new CredentialsConfig(null, null, "artifactory-credentials")
// def resolverCredentialsConfig = new CredentialsConfig(null, null, "artifactory-credentials")

// def artifactoryServer = new ArtifactoryServer(
//     "Artifactory-Server", // Name
//     "http://artifactory:8081/artifactory", // URL
//     deployerCredentialsConfig, // Deployer credentials config
//     resolverCredentialsConfig, // Resolver credentials config
//     300, // Timeout (in seconds)
//     false, // Bypass proxy
//     3, // Connection retry
//     10 // Deployment threads
// )

// // Set the Artifactory configuration
// def artifactoryConfig = instance.getDescriptorByType(org.jfrog.hudson.ArtifactoryBuilder.DescriptorImpl)
// artifactoryConfig.setArtifactoryServers([artifactoryServer])
// artifactoryConfig.save()
// instance.save()
