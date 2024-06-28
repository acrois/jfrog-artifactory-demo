#!groovy

import jenkins.model.*
import hudson.util.*
import jenkins.util.*
import hudson.PluginManager
import java.util.logging.Level
import java.util.logging.Logger

def logger = Logger.getLogger("Plugins")

def installPlugin(pluginId, version = null) {
    def pluginManager = Jenkins.instance.pluginManager
    def updateCenter = Jenkins.instance.updateCenter

    def installed = pluginManager.getPlugin(pluginId)
    if (installed != null) {
        logger.log(Level.INFO, "Plugin already installed: " + pluginId)
        return
    }

    def pluginWithVersion = version ? "${pluginId}:${version}" : pluginId
    def plugin = updateCenter.getPlugin(pluginId)

    if (plugin) {
        plugin.deploy(true)
        logger.log(Level.INFO, "Installing plugin: " + pluginWithVersion)
    } else {
        logger.log(Level.SEVERE, "Plugin not found: " + pluginId)
    }
}

def pluginList = [
    [id: 'git', version: '5.2.2'],
    [id: 'workflow-aggregator', version: '596.v8c21c963d92d'],
    [id: 'docker-workflow', version: '580.vc0c340686b_54'],
    [id: 'credentials-binding', version: '677.vdc9d38cb_254d'],
    [id: 'artifactory', version: '4.0.6'],
    [id: 'ws-cleanup', version: '0.46']
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
