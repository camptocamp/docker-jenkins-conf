def env = System.getenv()

def github_user = env['JENKINS_GITHUB_USER']
def github_org = env['JENKINS_GITHUB_ORG']
def github_base_job_dsl_repo = env['JENKINS_BASE_JOB_DSL_REPO']
def jenkins_admin = env['JENKINS_ADMIN_GROUPNAME']

def github_cred_id = "${github_user}-token"

def job_name = github_org + '-base-job'
def github_repo = github_org + '/' + github_base_job_dsl_repo

job(job_name) {
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
            ignoreExisting()
        }
    }
}