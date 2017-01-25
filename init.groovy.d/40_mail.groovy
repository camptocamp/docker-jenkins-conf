#!groovy
import jenkins.model.*

def inst = Jenkins.getInstance()
def env = System.getenv()
def desc = inst.getDescriptor("hudson.tasks.Mailer")

desc.setSmtpAuth(env['JENKINS_MAIL_USER'], env['JENKINS_MAIL_PASSWORD'])
desc.setReplyToAddress(env['JENKINS_MAIL_ADDRESS'])
desc.setSmtpHost(env['JENKINS_MAIL_SMTP_HOST'])
desc.setUseSsl(env['JENKINS_MAIL_SMTP_SSL'].toBoolean())
desc.setSmtpPort(env['JENKINS_MAIL_SMTP_PORT'].toString())
desc.setCharset("UTF-8")

desc.save()
inst.save()

// Set the administrator email address
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setAdminAddress(env['JENKINS_MAIL_ADDRESS'])
jenkinsLocationConfiguration.save()
