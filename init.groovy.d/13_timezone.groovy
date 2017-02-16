#!groovy
def env = System.getenv()
System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', env['JENKINS_TIMEZONE'])