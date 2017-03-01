def env = System.getenv()

def github_user = env['JENKINS_GITHUB_ADMIN_USER']
def github_org = env['JENKINS_GITHUB_ORG']
def github_base_job_dsl_repo = env['JENKINS_INITIAL_DSL_REPO']
def jenkins_admin = env['JENKINS_ADMIN_GROUPNAME']

def github_cred_id = "${github_user}-admin-token"

def folder_name = "admin"
def job_name = "00_initial_dsl_job"
def github_repo = github_org + '/' + github_base_job_dsl_repo

job("${folder_name}/${job_name}") {
    logRotator(-1, 10)
    scm {
        git {
            remote {
                github(github_repo)
                credentials(github_cred_id)
            }
        }
    }
    authorization {
        permissionAll(jenkins_admin)
    }
    steps {
        dsl {
            external('*.groovy')
            removeAction('DELETE')
        }
    }
}