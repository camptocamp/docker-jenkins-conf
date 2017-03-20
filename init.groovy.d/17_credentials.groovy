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
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey;
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

def github_user = env['JENKINS_GITHUB_USER']
def github_token = env['JENKINS_GITHUB_TOKEN']
def github_private_key = env['JENKINS_GITHUB_SSH_PRIVATE_KEY']

if (github_user && github_token && github_private_key) {

  println "Add token credentials for ${github_user} in SYSTEM scope"
  def sysTokenCredId = "${github_user}-system-token"
  Credentials sysTokenCred = (Credentials) new StringCredentialsImpl(
    CredentialsScope.SYSTEM,
    sysTokenCredId,
    "Github system token for ${github_user}",
    Secret.fromString(github_token)
  )
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), sysTokenCred)

  println "Add token as user/pw credentials for ${github_user} in SYSTEM scope"
  def sysPwCredId = "${github_user}-system-user-pw-token"
  Credentials sysPwCred = (Credentials) new UsernamePasswordCredentialsImpl(
    CredentialsScope.SYSTEM,
    sysPwCredId,
    "Github system user/password with token for ${github_user}",,
    github_user,
    github_token
  )
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), sysPwCred)

  println "Add ssh credentials (private key) for ${github_user} in SYSTEM scope"
  def sysSshCredId = "${github_user}-system-ssh"
  Credentials sysSshCred = new BasicSSHUserPrivateKey(
    CredentialsScope.SYSTEM,
    github_user,
    sysSshCredId,
    new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(github_private_key),
    "",
    ""
  )
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), sysSshCred)

  println "Add token credentials for ${github_user} in admin folder scope"
  def adminFolderTokenCredId = "${github_user}-system-token"
  Credentials adminFolderTokenCred = (Credentials) new StringCredentialsImpl(
    CredentialsScope.SYSTEM,
    adminFolderTokenCredId,
    "Github system token for ${github_user}",
    Secret.fromString(github_token)
  )

  println "Add token as user/pw credentials for ${github_user} in admin folder scope"
  def adminFolderPwCredId = "${github_user}-admin-user-pw-token"
  Credentials adminFolderPwCred = (Credentials) new UsernamePasswordCredentialsImpl(
    CredentialsScope.GLOBAL,
    adminFolderPwCredId,
    "Github admin user/password with token for ${github_user}",,
    github_user,
    github_token
  )

  println "Add ssh credentials (private key) for ${github_user} in admin folder scope"
  def adminFoldersshCredId = "${github_user}-admin-ssh"

  Credentials adminFoldersshCred = new BasicSSHUserPrivateKey(
    CredentialsScope.GLOBAL,
    github_user,
    adminFoldersshCredId,
    new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(github_private_key),
    "",
    ""
  )

  jenkins = Jenkins.instance
  for (folder in jenkins.getAllItems(Folder.class)) {
    if(folder.name.equals('admin')){
  	AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
      FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class)
      if(property) {
          println "Add Github token credentials in admin folder's GLOBAL scope"
          property.getStore().addCredentials(Domain.global(), adminFolderTokenCred)
          println "Add Github token as user/pw credentials in admin folder's GLOBAL scope"
          property.getStore().addCredentials(Domain.global(), adminFolderPwCred)
          println "Add Github ssh credentials in admin folder's GLOBAL scope"
          property.getStore().addCredentials(Domain.global(), adminFoldersshCred)
      } else {
          println "Initialize Folder Credentials store and add Github token and ssh credentials in admin folder's GLOBAL scope"
          property = new FolderCredentialsProperty([adminFolderTokenCred, adminFolderPwCred, adminFoldersshCred])
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