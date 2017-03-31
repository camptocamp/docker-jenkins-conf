FROM openjdk:8-jdk

VOLUME ["/var/jenkins_home/init.groovy.d"]
VOLUME ["/var/jenkins_home/job-dsl-scripts"]
VOLUME ["/var/jenkins_home/.keystore"]
VOLUME ["/var/jenkins_home/plugins"]

ENV JENKINS_VERSION=2.46.1

RUN mkdir -p /usr/local/bin/ \
  && mkdir -p /usr/share/jenkins

# Download jenkins war of current jenkins version to determine the included plugins
RUN curl -fsSLO https://updates.jenkins-ci.org/download/war/$JENKINS_VERSION/jenkins.war \
  && cp jenkins.war /usr/share/jenkins \
  && rm -f jenkins.war

ADD init.groovy.d/*.groovy /var/jenkins_home/init.groovy.d/
ADD job-dsl-scripts/* /var/jenkins_home/job-dsl-scripts/
ADD jenkins-support /usr/local/bin/
ADD install-plugins.sh /usr/local/bin/
ADD plugins.txt /
ADD import-crt.sh /
ADD entrypoint.sh /

RUN chmod +x /usr/local/bin/install-plugins.sh \
  && chmod +x /import-crt.sh \
  && chmod +x /entrypoint.sh \
  && chown -R 1000.1000 /var/jenkins_home/init.groovy.d \
  && chown -R 1000.1000 /var/jenkins_home/job-dsl-scripts \
  && chown -R 1000.1000 /var/jenkins_home/plugins

RUN export JENKINS_UC="http://updates.jenkins-ci.org" \
  && /usr/local/bin/install-plugins.sh $(cat ./plugins.txt)

ENTRYPOINT [ "/entrypoint.sh" ]
