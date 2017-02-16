#!groovy
import jenkins.model.*

def inst = Jenkins.getInstance()
def env = System.getenv()

// Set the administrator email address
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setAdminAddress(env['JENKINS_MAIL_ADDRESS'])
jenkinsLocationConfiguration.setUrl(env['JENKINS_ROOT_URL'])
jenkinsLocationConfiguration.save()
