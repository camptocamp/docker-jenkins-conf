FROM openjdk:8-jdk

VOLUME ["/var/jenkins_home/init.groovy.d"]
VOLUME ["/var/jenkins_home/.keystore"]

ADD init.groovy.d/*.groovy /var/jenkins_home/init.groovy.d/
ADD job-dsl-scripts/*.groovy /var/jenkins_home/job-dsl-scripts/

ADD import-crt.sh /

RUN chmod +x /import-crt.sh

ENTRYPOINT [ "/import-crt.sh" ]
