#!groovy
import jenkins.model.Jenkins
import jenkins.model.*
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement

def env = System.getenv()
def user = env['JENKINS_GITHUB_ADMIN_USER']
def jenkins_admin = env['JENKINS_ADMIN_GROUPNAME']
def folder_name = 'admin'

def jobDslScript = """
folder('${folder_name}') {
            displayName('${folder_name}')
            description('Admin generation jobs folder')
            authorization {
                permissionAll('${jenkins_admin}')
            }
        }
"""
def workspace = new File('.')
def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
println new DslScriptLoader(jobManagement).runScript(jobDslScript)
