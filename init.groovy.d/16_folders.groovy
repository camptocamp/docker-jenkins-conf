#!groovy
import jenkins.model.Jenkins
import jenkins.model.*
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement

def env = System.getenv()
def instance = Jenkins.getInstance()
def teams = env['JENKINS_TEAMS']

teams.eachLine { line ->
  def team_params  = line.split(';')
  def team_ldap_group = team_params[0]
  def team_name = team_params[0]
  if (team_params.size() > 1) {
    team_name = team_params[1]
  }

  def jobDslScript = """
folder('${team_name}') {
            displayName('${team_name}')
            description('CI environment for ${team_name}')
            authorization {
                permissionAll('${team_ldap_group}')
            }
        }
"""
  def workspace = new File('.')
  def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
  println new DslScriptLoader(jobManagement).runScript(jobDslScript)
}