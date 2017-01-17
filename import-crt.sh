#!/bin/sh

# create a custom keystore
CUSTOM_KEYSTORE=$JENKINS_HOME/.keystore/
mkdir -p $CUSTOM_KEYSTORE
cp $JAVA_HOME/jre/lib/security/cacerts $CUSTOM_KEYSTORE

# import out certs
echo "$JENKINS_LDAP_CA_CRT" > ca.crt
$JAVA_HOME/bin/keytool -keystore $JENKINS_HOME/.keystore/cacerts -import -file ca.crt -keypass changeit -storepass changeit -noprompt

exec "$@"