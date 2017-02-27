import jenkins.model.*
import java.util.logging.Logger

def env = System.getenv()

def instance = Jenkins.getInstance()
def plugins = env['JENKINS_PLUGINS']

def logger = Logger.getLogger("")
def need_reload = false

def pm = instance.getPluginManager()

plugins.eachLine {
    plugin_line = it.split(':')
    name = plugin_line[0]
    logger.info("Checking " + name)
    plugin = pm.getPlugin(name)
    if (!plugin) {
        logger.info(name + " not active. Restarting")
        need_reload = true
    } else {
        logger.info(name + " already activated")
    }
}

if (need_reload) {
  logger.info("Plugins installed, initializing a restart!")
  instance.doSafeRestart()
}
