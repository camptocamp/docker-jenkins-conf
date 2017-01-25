#!groovy
import groovy.io.FileType
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement

def env = System.getenv()
def dslDir = new File("${env['JENKINS_HOME']}/job-dsl-scripts/")

dslDir.eachFileMatch(FileType.ANY, ~/.*.groovy$/){ jobDslScript ->
  println "Applying ${jobDslScript.name}"
  def workspace = new File('.')
  def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
  println new DslScriptLoader(jobManagement).runScript(jobDslScript.text)
}