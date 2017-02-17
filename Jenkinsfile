pipeline {
  environment {
     last_tag = sh(script: "git describe --abbrev=0 --tags > .git/last-tag", returnStdout: true)
  }
  agent {
    label 'docker'
  }
  stages {
    stage('build docker image') {
      steps {
        sh "docker build -t 'camptocamp/jenkins-conf:${env.last_tag}' ."
      }
    }
  }
}