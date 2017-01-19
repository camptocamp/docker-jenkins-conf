import jenkins.model.*

def inst = Jenkins.getInstance()

def desc = inst.getDescriptor("hudson.tasks.Mailer")

desc.setSmtpAuth(env['JENKINS_MAIL_USER'], env['JENKINS_MAIL_PASSWORD'])
desc.setReplyToAddress(env['JENKINS_MAIL_REPLY'])
desc.setSmtpHost(env['JENKINS_MAIL_SMTP_HOST'])
desc.setUseSsl(env['JENKINS_MAIL_SMTP_SSL'])
desc.setSmtpPort(env['JENKINS_MAIL_SMTP_PORT'])
desc.setCharset("UTF-8")

desc.save()
inst.save()

import jenkins.model.*
