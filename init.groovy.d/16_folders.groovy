#!groovy
import jenkins.model.Jenkins
import jenkins.model.*
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement

def env = System.getenv()
def instance = Jenkins.getInstance()
def teams = env['JENKINS_TEAMS']

teams.eachLine { team ->

  def jobDslScript = """
folder('${team}') {
            displayName('${team}')
            description('Folder for ${team}')
            authorization {
                permissionAll('${team}')
            }
        }
"""
  def workspace = new File('.')
  def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
  println new DslScriptLoader(jobManagement).runScript(jobDslScript)
}