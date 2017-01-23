#!groovy
import jenkins.model.*
import hudson.security.ProjectMatrixAuthorizationStrategy
import hudson.security.AuthorizationStrategy
import hudson.security.Permission
import jenkins.security.s2m.AdminWhitelistRule

// Enable Slave -> Master Access Control
Jenkins.instance.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);
Jenkins.instance.save()

def env = System.getenv()

// JVM did not like 'hypen' in the class name, it will crap out saying it is
// illegal class name.
class BuildPermission {
  static buildNewAccessList(userOrGroup, permissions) {
    def newPermissionsMap = [:]
    permissions.each {
      newPermissionsMap.put(Permission.fromId(it), userOrGroup)
    }
    return newPermissionsMap
  }
}

if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "matrix-auth" } != null ) {
  if ( Jenkins.instance.isUseSecurity() ) {
    println "--> setting project matrix authorization strategy"
    strategy = new hudson.security.ProjectMatrixAuthorizationStrategy()

    //------------------- Authenticated (standard user)---------------------------------------
    authenticatedPermissions = [
      "hudson.model.Hudson.Read",
    ]

    authenticated = BuildPermission.buildNewAccessList("authenticated", authenticatedPermissions)
    authenticated.each { p, u -> strategy.add(p, u) }

    //----------------- Jenkins Admin -----------------------------------------
    def strategy = new hudson.security.ProjectMatrixAuthorizationStrategy()
    strategy.add(Jenkins.ADMINISTER, env['JENKINS_ADMIN_GROUPNAME'])

    jenkinsAdminPermissions = []
    // plugin 'credentials' permissions
    if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "credentials" } != null ){
      jenkinsAdminPermissions.addAll(["com.cloudbees.plugins.credentials.CredentialsProvider.Create",
                                      "com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
                                      "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
                                      "com.cloudbees.plugins.credentials.CredentialsProvider.Update",
                                      "com.cloudbees.plugins.credentials.CredentialsProvider.View"])
    }

    // plugin 'gerrit-trigger' permissions
    if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "gerrit-trigger" } != null ){
      jenkinsAdminPermissions.addAll(["com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.ManualTrigger",
                                      "com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.Retrigger"])
    }
    // plugin 'promoted-builds' permissions
    if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "promoted-builds" } != null ){
      jenkinsAdminPermissions.addAll(["hudson.plugins.promoted_builds.Promotion.Promote"])
    }

    jenkinsAdmin = BuildPermission.buildNewAccessList(env['JENKINS_ADMIN_GROUPNAME'], jenkinsAdminPermissions)
    jenkinsAdmin.each { p, u -> strategy.add(p, u) }

    //----------------- Jenkins Swarm (slave node auth) -----------------------------------------
    jenkinsSwarmPermissions = [
      "hudson.model.Computer.Build",
      "hudson.model.Computer.Build",
      "hudson.model.Computer.Configure",
      "hudson.model.Computer.Connect",
      "hudson.model.Computer.Create",
      "hudson.model.Computer.Delete",
      "hudson.model.Computer.Disconnect",
     ]

    jenkinsAdmin = BuildPermission.buildNewAccessList(env['JENKINS_SWARM_USERNAME'], jenkinsSwarmPermissions)
    jenkinsAdmin.each { p, u -> strategy.add(p, u) }

    //-------------------------------------------------------------------------

    // now set the strategy globally
    Jenkins.instance.setAuthorizationStrategy(strategy)
    Jenkins.instance.save()
  }
}
