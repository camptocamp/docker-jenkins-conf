pipeline {
  environment {
     FOO = "foo"
  }
  agent {
    label 'docker'
  }
  stages {
    stage('build docker image') {
      steps {
        sh "git describe --abbrev=0 --tags > .git/last-tag"
        script {
          def last_tag = readFile('.git/last-tag')
          env['last_tag'] = last_tag
        }
        sh "docker build -t 'camptocamp/jenkins-conf:${env.last_tag}'' ."
      }
    }
  }
}