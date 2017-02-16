#!groovy
import jenkins.model.*
import hudson.model.*
import hudson.slaves.*
import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.nodes.JobRestrictionProperty;
import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.restrictions.job.RegexNameRestriction;
import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.util.GroupSelector;

// Restrict master node executor access
def regularSlaves = Jenkins.instance.computers.grep{
  it.class.superclass?.simpleName != 'AbstractCloudComputer'
}
def masterComputer = regularSlaves.find { it.displayName =~ "master" }

def regex = "^admin.*"
RegexNameRestriction regexRestr = new RegexNameRestriction(regex, false );
JobRestrictionProperty jobrestrict = new JobRestrictionProperty(regexRestr);

masterNode = masterComputer.getNode()
masterNode.getNodeProperties().add(jobrestrict)
masterNode.save()