final IMAGE_BASE_NAME = 'camptocamp/jenkins-conf'
final DOCKER_REGISTRY_URL = 'https://registry.hub.docker.com'

node("docker") {
    sh 'git describe --abbrev=0 --tags > .git/last-tag'
    def tag = readFile('.git/last-tag').trim()

    stage "build ${IMAGE_BASE_NAME}"
    def app = docker.build "${IMAGE_BASE_NAME}:${tag}"

    stage "publish tag ${tag} of ${IMAGE_BASE_NAME}"
    withCredentials(
        [[$class: 'UsernamePasswordMultiBinding',
        credentialsId: 'dockerhubc2c',
        usernameVariable: 'USERNAME',
        passwordVariable: 'PASSWORD']]
    ){
        sh 'docker login -u "$USERNAME" -p "$PASSWORD"'
        docker.image("${IMAGE_BASE_NAME}:${tag}").push()
    }
}