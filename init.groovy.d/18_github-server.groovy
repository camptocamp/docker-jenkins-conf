#!/usr/bin/env groovy
import jenkins.model.Jenkins
import org.jenkinsci.plugins.github.config.GitHubServerConfig

def env = System.getenv()
def user = env['JENKINS_GITHUB_USER']
def github_api_url = "https://api.github.com"

def githubServerConfig = new GitHubServerConfig("${user}-token-secret-text")
githubServerConfig.setManageHooks(false)
githubServerConfig.setApiUrl(github_api_url)

def githubPluginConfig = Jenkins.instance.getExtensionList('org.jenkinsci.plugins.github.config.GitHubPluginConfig')[0]
githubPluginConfig.setConfigs([githubServerConfig])
githubPluginConfig.save()