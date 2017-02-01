import jenkins.model.*
import com.cloudbees.hudson.plugins.folder.*;
import com.cloudbees.hudson.plugins.folder.properties.*;
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

def env = System.getenv()

def user = env['JENKINS_GITHUB_USER']
def credId = "${user}-token"
def description = "Github token for ${user}"
def password = env['JENKINS_GITHUB_TOKEN']

Credentials c = (Credentials) new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  credId,
  description,
  user,
  password
)

jenkins = Jenkins.instance
for (folder in jenkins.getAllItems(Folder.class)) {
  if(folder.name.equals('admin')){
	AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
    FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class)
    if(property) {
        println "Add credentials in global store"
        property.getStore().addCredentials(Domain.global(), c)
    } else {
        println "Initialize Folder Credentials store and add credentials in global store"
        property = new FolderCredentialsProperty([c])
        folderAbs.addProperty(property)
    }
    println property.getCredentials().toString()
  }
}


