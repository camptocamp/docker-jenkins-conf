#!/bin/bash

# download plugins
export JENKINS_UC="http://mirrors.jenkins-ci.org"

echo "downloading custom plugins"
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

chown -R 1000.1000 /var/jenkins_home/plugins

# import ldap server ssl certificate
./import-crt.sh

exec "$@"
