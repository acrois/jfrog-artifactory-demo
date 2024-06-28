#!groovy

import jenkins.model.*
import hudson.util.*
import jenkins.util.*
import hudson.PluginManager
import java.util.logging.Level
import java.util.logging.Logger

def logger = Logger.getLogger("Plugins")
def instance = Jenkins.getInstance()
def pluginManager = instance.getPluginManager()
def updateCenter = instance.getUpdateCenter()

def installPlugin(pluginId, version = null) {
    def plugin = updateCenter.getPlugin(pluginId)

    if (plugin) {
        if (version) {
            plugin.deploy(version)
        } else {
            plugin.deploy()
        }
    } else {
        logger.log(Level.SEVERE, "Plugin not found: " + pluginId)
    }
}

def pluginList = [
    [id: 'git', version: '5.2.2'],
    [id: 'workflow-aggregator', version: '596.v8c21c963d92d'],
    [id: 'docker-workflow', version: '580.vc0c340686b_54'],
    [id: 'credentials-binding', version: '677.vdc9d38cb_254d'],
    [id: 'artifactory', version: '4.0.6']
]

pluginList.each { plugin ->
    def pluginId = plugin.id
    def version = plugin.version

    if (!Jenkins.instance.pluginManager.getPlugin(pluginId)) {
        logger.log(Level.INFO, "Installing plugin: " + pluginId + ":" + version)
        installPlugin(pluginId, version)
    } else {
        logger.log(Level.INFO, "Plugin already installed: " + pluginId)
    }
}
