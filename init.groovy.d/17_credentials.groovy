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

def user = env['JENKINS_GITHUB_USER']
def description = "Github token for ${user}"
def token = env['JENKINS_GITHUB_TOKEN']

// add admin credential for github plugin (will be system only)
def textCredId = "${user}-token-secret-text"
Credentials textc = (Credentials) new StringCredentialsImpl(
  CredentialsScope.SYSTEM,
  textCredId,
  description,
  Secret.fromString(token)
)

println "Add admin text credentials"
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), textc)

// add admin credential for pipeline generation (will be scoped in admin folder)
def pwCredId = "${user}-token"
Credentials pwc = (Credentials) new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  pwCredId,
  description,
  user,
  token
)

jenkins = Jenkins.instance
for (folder in jenkins.getAllItems(Folder.class)) {
  if(folder.name.equals('admin')){
	AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
    FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class)
    if(property) {
        println "Add admin credentials"
        property.getStore().addCredentials(Domain.global(), pwc)
    } else {
        println "Initialize Folder Credentials store and add credentials in global store"
        property = new FolderCredentialsProperty([pwc])
        folderAbs.addProperty(property)
    }
  }
}


