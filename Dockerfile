FROM alpine

VOLUME ["/var/jenkins_home/init.groovy.d"]

ADD scripts/* /var/jenkins_home/init.groovy.d/

ENTRYPOINT [ "/bin/true" ]
