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
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
