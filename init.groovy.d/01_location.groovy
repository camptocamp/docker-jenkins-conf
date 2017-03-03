#!groovy
import jenkins.model.*
import java.lang.System
import hudson.model.*
import jenkins.model.*

def inst = Jenkins.getInstance()
def env = System.getenv()
def jenkins_mail = env['JENKINS_MAIL_ADDRESS']
def jenkins_url = env['JENKINS_ROOT_URL']

// Set the administrator email address
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
if (jenkins_url) {
  // set url
  println "Set root URL to '${jenkins_url}'"
  jenkinsLocationConfiguration.setUrl(jenkins_url)
}
if (jenkins_mail) { jenkinsLocationConfiguration.setAdminAddress(jenkins_mail) }
jenkinsLocationConfiguration.save()
inst.save()

systemMessage = "This Jenkins instance is generated from code.\n " +
                "Avoid any manual changes since they will be discarded with new deployment.\n "

println "Set system message to:\n ${systemMessage}"
Jenkins.instance.setSystemMessage(systemMessage)
