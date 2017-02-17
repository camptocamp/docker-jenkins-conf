final IMAGE_BASE_NAME = 'camptocamp/jenkins-conf'

node("docker") {
    docker.withRegistry('dockerhub', 'dockerhubc2c') {
        sh 'git describe --abbrev=0 --tags > .git/last-tag'
        def tag = readFile('.git/last-tag').trim()
        stage "build"
        def app = docker.build "${IMAGE_BASE_NAME}:${tag}"
        stage "publish"
        app.push tag
    }
}