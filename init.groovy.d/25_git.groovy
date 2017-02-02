import jenkins.model.*

def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.plugins.git.GitSCM")
def env = System.getenv()

desc.setGlobalConfigName("jenkins")
desc.setGlobalConfigEmail(env['JENKINS_MAIL_ADDRESS'])

desc.save()