FROM openjdk:8-jdk

VOLUME ["/var/jenkins_home/init.groovy.d"]
VOLUME ["/var/jenkins_home/job-dsl-scripts"]
VOLUME ["/var/jenkins_home/.keystore"]
VOLUME ["/var/jenkins_home/plugins"]
VOLUME ["/usr/share/jenkins"]

RUN mkdir -p /usr/local/bin/

ADD init.groovy.d/*.groovy /var/jenkins_home/init.groovy.d/
ADD job-dsl-scripts/* /var/jenkins_home/job-dsl-scripts/
ADD jenkins-support /usr/local/bin/
ADD install-plugins.sh /usr/local/bin/
ADD import-crt.sh /
ADD entrypoint.sh /

RUN chmod +x /usr/local/bin/install-plugins.sh
RUN chmod +x /import-crt.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]
