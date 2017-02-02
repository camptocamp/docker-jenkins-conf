#!groovy
import hudson.model.*;
import jenkins.model.*;

Thread.start {
      sleep 10000
      println "--> setting agent port for jnlp"
      def env = System.getenv()
      int port = env['JENKINS_SLAVE_AGENT_PORT'].toInteger()

      def inst = Jenkins.getInstance()
      inst.setSlaveAgentPort(port)
      inst.save()
      println "--> setting agent port for jnlp... done"
}
