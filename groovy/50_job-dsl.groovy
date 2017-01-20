import hudson.model.*
import jenkins.model.*;
import javaposse.jobdsl.plugin.*;

Thread.start {
    sleep 10000

    def jenkins = Jenkins.getInstance()

    //Define a job name
    def jobName = "Seed"

    //Instantiate a new project , canRoam= true
    def job = new FreeStyleProject(jenkins, jobName)
    job.setAssignedLabel(null);

    job.setCustomWorkspace("/var/jenkins_home/init.groovy.d/job-dsl")

    def ExecuteDslScripts.ScriptLocation scriptlocationFileSys = new ExecuteDslScripts.ScriptLocation('false', "*.json", null);
    def ExecuteDslScripts executeDslScripts = new ExecuteDslScripts(scriptlocationFileSys, false, RemovedJobAction.IGNORE);

    job.buildersList.add(executeDslScripts)

    jenkins.add(job, job.getName());
    jenkins.reload()

    // Run job
    def jobRef = jenkins.getItem(job.getName());
    jenkins.getQueue().schedule(jobRef,10);

}
