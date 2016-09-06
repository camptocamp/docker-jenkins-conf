FROM alpine

ENV JENKINS_ADMIN_USERNAME
ENV JENKINS_ADMIN_PASSWORD
ENV JENKINS_PLUGINS

VOLUME ["/usr/share/jenkins/ref/init.groovy.d"]

ADD scripts/* /usr/share/jenkins/ref/init.groovy.d/

ENTRYPOINT [ "/bin/true" ]
