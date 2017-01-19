#!/bin/sh

# create a custom keystore location
CUSTOM_KEYSTORE=$JENKINS_HOME/.keystore/
mkdir -p $CUSTOM_KEYSTORE
# copy system keystore to the custom location
cp $JAVA_HOME/jre/lib/security/cacerts $CUSTOM_KEYSTORE

# import certs
echo "$JENKINS_LDAP_CA_CRT" > ca.crt
$JAVA_HOME/bin/keytool -keystore $JENKINS_HOME/.keystore/cacerts -import -trustcacerts -file ca.crt -keypass changeit -storepass changeit -noprompt

exec "$@"
