import jenkins.model.Jenkins;
import hudson.model.FreeStyleProject;

job = Jenkins.instance.createProject(FreeStyleProject, 'seed-job')
job.displayName = 'Seed Job'
job.setCustomWorkspace("/var/jenkins_home/init.groovy.d/job-dsl")

builder = new javaposse.jobdsl.plugin.ExecuteDslScripts(
  new javaposse.jobdsl.plugin.ExecuteDslScripts.ScriptLocation(
      'false',
      '*',
      null),
  false,
  javaposse.jobdsl.plugin.RemovedJobAction.DELETE,
  javaposse.jobdsl.plugin.RemovedViewAction.DELETE,
  javaposse.jobdsl.plugin.LookupStrategy.JENKINS_ROOT
)
job.buildersList.add(builder)

job.save()