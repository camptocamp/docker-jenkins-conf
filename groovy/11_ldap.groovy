#!groovy
import jenkins.model.Jenkins;
import hudson.security.FullControlOnceLoggedInAuthorizationStrategy;
import hudson.security.LDAPSecurityRealm;

def instance = Jenkins.getInstance()
def env = System.getenv()

instance.authorizationStrategy = new FullControlOnceLoggedInAuthorizationStrategy() ;

if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "ldap" } != null ){
  instance.securityRealm= new LDAPSecurityRealm(
    server = env['JENKINS_LDAP_SERVER'],
    rootDN = env['JENKINS_LDAP_ROOT_DN'],
    userSearchBase = env['JENKINS_LDAP_USER_SEARCH_BASE'],
    userSearch = env['JENKINS_LDAP_USER_SEARCH_FILTER'],
    groupSearchBase = env['JENKINS_LDAP_GROUP_SEARCH_BASE'],
    groupSearchFilter = env['JENKINS_LDAP_GROUP_SEARCH_FILTER'],
    groupMembershipStrategy = null,
    managerDN = env['JENKINS_LDAP_MANAGER_USER_DN'],
    managerPasswordSecret = env['JENKINS_LDAP_MANAGER_USER_PASSWORD'],
    inhibitInferRootDN = false,
    disableMailAddressResolver = false,
    cache = null
    );

  instance.save()
}