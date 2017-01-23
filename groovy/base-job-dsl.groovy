def env = System.getenv()

def github_token = env['JENKINS_GITHUB_TOKEN']
def github_org = env['JENKINS_GITHUB_ORG']
def github_base_job_dsl_repo = env['JENKINS_BASE_JOB_DSL_REPO']
def jenkins_admin = env['JENKINS_ADMIN_GROUPNAME']

def job_name = github_org + ' Base job'
def github_repo = github_org + '/' + github_base_job_dsl_repo

job(job_name) {
    authenticationToken(github_token)
    logRotator(-1, 10)
    scm {
        github(github_repo, 'master')
    }
    authorization {
        permissionAll(jenkins_admin)
    }
    steps {
        dsl {
            external('*.groovy')
            removeAction('DELETE')
            ignoreExisting()
            additionalClasspath('lib')
        }
    }
}