def env = System.getenv()

def github_user = env['JENKINS_GITHUB_USER']
def github_org = env['JENKINS_GITHUB_ORG']
def github_base_job_dsl_repo = env['JENKINS_BASE_JOB_DSL_REPO']
def jenkins_admin = env['JENKINS_ADMIN_GROUPNAME']
def teams = env['JENKINS_TEAMS']

def github_cred_id = "${github_user}-token"

def job_name = "Auto-Generate Pipelines"
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
        teams.eachLine { line ->
            def team_params  = line.split(';')
            def team_ldap_group = team_params[0]
            permission('hudson.model.Item.Build', team_ldap_group)
            permission('hudson.model.Item.Read', team_ldap_group)
        }
    }
    steps {
        dsl {
            external('*.groovy')
            removeAction('DELETE')
            ignoreExisting()
        }
    }
}