#!groovy
import jenkins.model.*
import com.cloudbees.hudson.plugins.folder.*;
import com.cloudbees.hudson.plugins.folder.properties.*;
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import org.jenkinsci.plugins.plaincredentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import com.cloudbees.plugins.credentials.domains.*;
import hudson.util.Secret

def env = System.getenv()

def hipchat_token = env['JENKINS_HIPCHAT_TOKEN']
if (hipchat_token) {
  // add admin credential for github plugin (will be system only)
  def hipchatTextCredId = "hipchat-global-token"
  Credentials hipchatTextc = (Credentials) new StringCredentialsImpl(
    CredentialsScope.GLOBAL,
    hipchatTextCredId,
    "HipChat token for Jenkins",
    Secret.fromString(hipchat_token)
  )

  println "Add HipChat token text credentials in GLOBAL scope"
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), hipchatTextc)
}

def github_user = env['JENKINS_GITHUB_ADMIN_USER']
def github_token = env['JENKINS_GITHUB_ADMIN_TOKEN']

if (github_user && github_token) {
  def textCredId = "${github_user}-system-token"
  Credentials textc = (Credentials) new StringCredentialsImpl(
    CredentialsScope.SYSTEM,
    textCredId,
    "Github system token for ${github_user}",
    Secret.fromString(github_token)
  )

  println "Add Github token text credentials in SYSTEM scope"
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), textc)

  def pwCredIds = "${github_user}-system-user-pw-token"
  Credentials pwcs = (Credentials) new UsernamePasswordCredentialsImpl(
    CredentialsScope.SYSTEM,
    pwCredIds,
    "Github system user/password with token for ${github_user}",,
    github_user,
    github_token
  )

  println "Add Github token credentials in SYSTEM scope"
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), pwcs)

  // add admin credential for pipeline generation (will be scoped in admin folder)
  def pwCredId = "${github_user}-admin-token"
  Credentials pwc = (Credentials) new UsernamePasswordCredentialsImpl(
    CredentialsScope.GLOBAL,
    pwCredId,
    "Github admin user/password with token for ${github_user}",,
    github_user,
    github_token
  )

  jenkins = Jenkins.instance
  for (folder in jenkins.getAllItems(Folder.class)) {
    if(folder.name.equals('admin')){
  	AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
      FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class)
      if(property) {
          println "Add Github token credentials in admin folder's GLOBAL scope"
          property.getStore().addCredentials(Domain.global(), pwc)
      } else {
          println "Initialize Folder Credentials store and add Github token credentials in admin folder's GLOBAL scope"
          property = new FolderCredentialsProperty([pwc])
          folderAbs.addProperty(property)
      }
    }
  }
}

def pipeline_token = env['JENKINS_GITHUB_PIPELINE_TOKEN']
if (pipeline_token) {

  def pwCredIdglob = "pipeline-library-token"
  Credentials pwcglob = (Credentials) new UsernamePasswordCredentialsImpl(
    CredentialsScope.GLOBAL,
    pwCredIdglob,
    "Read access on pipeline shared library",
    "pipeline-library",
    pipeline_token
  )

  println "Add Github token for pipeline library in GLOBAL scope"
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), pwcglob)
}