final IMAGES_BASE_NAME = 'camptocamp/saccas_suissealpine'
sh 'git describe --abbrev=0 --tags > .git/last-tag'
def tag = readFile('.git/last-tag').trim()

node("docker") {
    docker.withRegistry('dockerhub', 'dockerhubc2c') {
    stage "build"
    def app = docker.build "camptocamp/jenkins-conf:${tag}"
    stage "publish"
    app.push tag
}