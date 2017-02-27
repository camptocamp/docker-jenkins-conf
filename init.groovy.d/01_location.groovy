#!groovy
import jenkins.model.*

def inst = Jenkins.getInstance()
def env = System.getenv()
def jenkins_mail = env['JENKINS_MAIL_ADDRESS']
def jenkins_url = env['JENKINS_ROOT_URL']

// Set the administrator email address
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
if (jenkins_url) { jenkinsLocationConfiguration.setUrl(jenkins_url) }
if (jenkins_mail) { jenkinsLocationConfiguration.setAdminAddress(jenkins_mail) }
jenkinsLocationConfiguration.save()