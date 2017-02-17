node("docker") {

    //docker.withRegistry('<<your-docker-registry>>', '<<your-docker-registry-credentials-id>>') {

    stage "build"
    def last_tag = sh('git describe --abbrev=0 --tags > .git/last-tag')
    println last_tag
    def app = docker.build "camptocamp/jenkins-conf:${last_tag}"

    stage "publish"
    app.push last_tag
}