pipeline {
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

node("docker") {

    //docker.withRegistry('<<your-docker-registry>>', '<<your-docker-registry-credentials-id>>') {

    stage "build"
    def last_tag = git describe --abbrev=0 --tags > .git/last-tag
    println last_tag
    def app = docker.build "camptocamp/jenkins-conf:${last_tag}"

    stage "publish"
    app.push last_tag
}