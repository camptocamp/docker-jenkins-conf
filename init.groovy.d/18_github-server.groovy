#!groovy
import jenkins.model.Jenkins
import org.jenkinsci.plugins.github.config.GitHubServerConfig

def env = System.getenv()
def github_user = env['JENKINS_GITHUB_ADMIN_USER']
def github_api_url = "https://api.github.com"

if (github_user) {
  def githubServerConfig = new GitHubServerConfig("${github_user}-system-token")
  githubServerConfig.setManageHooks(false)
  githubServerConfig.setApiUrl(github_api_url)

  def githubPluginConfig = Jenkins.instance.getExtensionList('org.jenkinsci.plugins.github.config.GitHubPluginConfig')[0]
  githubPluginConfig.setConfigs([githubServerConfig])
  githubPluginConfig.save()
}
