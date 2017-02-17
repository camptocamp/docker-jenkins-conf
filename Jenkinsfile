pipeline {

  agent {
    label 'docker'
  }

  stages {

    stage('build docker image') {

      steps {
        sh "git describe --abbrev=0 --tags > .git/last-tag"
      }
      script{
        def last_tag = readFile('.git/last-tag').trim()
        println last_tag
      }
      steps {
        sh "docker build -t 'camptocamp/jenkins-conf:${last_tag}'' ."
      }

    }

  }

}