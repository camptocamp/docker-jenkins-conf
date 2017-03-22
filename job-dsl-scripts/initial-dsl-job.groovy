def env = System.getenv()

def github_initial_dsl_repo_url = env['JENKINS_INITIAL_DSL_REPO']
def github_initial_dsl_repo_branch = env['JENKINS_INITIAL_DSL_BRANCH']

def github_user = env['JENKINS_GITHUB_USER']
def github_org = env['JENKINS_GITHUB_ORG']
def github_config_repo = env['JENKINS_GITHUB_CONFIG_REPO']
def jenkins_admin = env['JENKINS_ADMIN_GROUPNAME']

if (github_user && github_org && github_config_repo && jenkins_admin) {

    def github_cred_id = "${github_user}-admin-user-pw-token"
    def folder_name = "admin"
    def job_name = "00_initial_dsl_job"
    def github_repo = github_org + '/' + github_config_repo

        job("${folder_name}/${job_name}") {
            logRotator(-1, 10)

            multiscm {
                git {
                    remote {
                        github(github_repo)
                        credentials(github_cred_id)
                    }
                    extensions {
                        relativeTargetDirectory('config')
                    }
                }
                git {
                    branch(github_initial_dsl_repo_branch)
                    remote {
                        url(github_initial_dsl_repo_url)
                    }
                }
            }

            authorization {
                permissionAll(jenkins_admin)
            }

            steps {
                dsl {
                    external('*.groovy')
                    ignoreExisting()
                    removeAction('DISABLE')
                    removeViewAction('DISABLE')
                }
            }
        }
}