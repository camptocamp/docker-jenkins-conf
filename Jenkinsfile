final IMAGE_BASE_NAME = 'camptocamp/jenkins-conf'
final DOCKER_REGISTRY_URL = 'https://registry.hub.docker.com'

node("docker") {
    docker.withRegistry($DOCKER_REGISTRY_URL, 'dockerhubc2c') {
        sh 'git describe --abbrev=0 --tags > .git/last-tag'
        def tag = readFile('.git/last-tag').trim()
        stage "build"
        def app = docker.build "${IMAGE_BASE_NAME}:${tag}"
        stage "publish"
        app.push tag
    }
}