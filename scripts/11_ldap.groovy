import jenkins.model.*
import hudson.security.*
import org.jenkinsci.plugins.*

def instance = Jenkins.getInstance()
def env = System.getenv()

String server = env['JENKINS_LDAP_SERVER']
String rootDN = env['JENKINS_LDAP_ROOT_DN']
String userSearchBase = env['JENKINS_LDAP_USER_SEARCH_BASE']
String userSearch = env['JENKINS_LDAP_USER_SEARCH_FILTER']
String groupSearchBase = env['JENKINS_LDAP_GROUP_SEARCH_BASE']
String groupSearchFilter = env['JENKINS_LDAP_GROUP_SEARCH_FILTER']
String managerDN = env['JENKINS_LDAP_MANAGER_USER_DN']
String managerPassword = env['JENKINS_LDAP_MANAGER_USER_PASSWORD']
boolean inhibitInferRootDN = false

SecurityRealm ldap_realm = new LDAPSecurityRealm(server, rootDN, userSearchBase, userSearch, groupSearchBase, groupSearchFilter, managerDN, managerPassword, inhibitInferRootDN)

instance.setSecurityRealm(ldap_realm)
instance.save()
instance.reload()