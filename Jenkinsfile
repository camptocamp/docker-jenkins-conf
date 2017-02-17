pipeline {
  environment {
  }
  agent {
    label 'docker'
  }
  stages {
    stage('build docker image') {
      steps {
        script {
          env['last_tag'] = sh(script: "git describe --abbrev=0 --tags > .git/last-tag", returnStdout: true)
        }
        sh "docker build -t 'camptocamp/jenkins-conf:${env.last_tag}' ."
      }
    }
  }
}