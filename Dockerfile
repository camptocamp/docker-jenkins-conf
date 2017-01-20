FROM openjdk:8-jdk

VOLUME ["/var/jenkins_home/init.groovy.d"]
VOLUME ["/var/jenkins_home/.keystore"]

ADD groovy/*.groovy /var/jenkins_home/init.groovy.d/

ADD import-crt.sh /

RUN chmod +x /import-crt.sh

ENTRYPOINT [ "/import-crt.sh" ]
