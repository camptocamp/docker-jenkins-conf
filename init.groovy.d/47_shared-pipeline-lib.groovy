import jenkins.model.Jenkins
import jenkins.plugins.git.GitSCMSource;
import hudson.model.FreeStyleProject
import hudson.plugins.git.UserRemoteConfig
import org.jenkinsci.plugins.workflow.libs.*
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;

def env = System.getenv()

def pipeline_lib_name = env['JENKINS_PIPELINE_LIB_NAME']
def pipeline_lib_repo = env['JENKINS_PIPELINE_LIB_REPO']
def pipeline_token = env['JENKINS_GITHUB_PIPELINE_TOKEN']

def textCredId = "pipeline-library-token"

if (pipeline_token && pipeline_lib_repo && pipeline_lib_name) {
  if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "workflow-cps-global-lib" } != null ) {
    println "--> setting shared pipeline library"

    def sharedLibRepo = pipeline_lib_repo
    def sharedLibName = pipeline_lib_name
    def credId = textCredId
    def ref = "master"

    def inst = Jenkins.getInstance()
    def desc = inst.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

    GitSCMSource scm = new GitSCMSource(
      sharedLibName,
      sharedLibRepo,
      textCredId,
      "*/${ref}",
      "",
      false
    )

    SCMSourceRetriever retriever = new SCMSourceRetriever(scm)

    LibraryConfiguration libconfig = new LibraryConfiguration(
      sharedLibName,
      retriever
    )

    libconfig.setDefaultVersion(ref)
    libconfig.setImplicit(false)

    desc.get().setLibraries([libconfig])
    desc.save()
  }
}