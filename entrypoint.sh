#!/bin/bash

# download jenkins war for bundled plugins info
if [ -z ${JENKINS_VERSION+x} ]
  then
  echo "JENKINS_VERSION is unset"
  exit 1
else
  echo "JENKINS_VERSION is set to '$JENKINS_VERSION'"
fi

if [ -e /usr/share/jenkins/jenkins.war ]
then
  war_jenkins_version=$(java -jar /usr/share/jenkins/jenkins.war --version)
  echo "war_jenkins_version is '$war_jenkins_version'"
fi

if [ "$war_jenkins_version" != "$JENKINS_VERSION" ]
  then
  curl -fSLO https://updates.jenkins-ci.org/download/war/$JENKINS_VERSION/jenkins.war
  cp jenkins.war /usr/share/jenkins
  rm -f jenkins.war
fi

# download plugins
export JENKINS_UC="http://updates.jenkins-ci.org"
/usr/local/bin/install-plugins.sh $JENKINS_PLUGINS

# install plugins in $JENKINS_HOME/plugins
export COPY_REFERENCE_FILE_LOG="/install-plugins.log"
source /usr/local/bin/jenkins-support
lsfns () {
  declare -F | cut -d" " -f3 | egrep -v "^_"
}
export -f $(lsfns)
find /usr/share/jenkins/ref/ -type f -exec bash -c 'copy_reference_file {}' \;
echo "$(cat /install-plugins.log)"

# import ldap server ssl certificate
./import-crt.sh

exec "$@"
