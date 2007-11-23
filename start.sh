#!/bin/sh

cd tomcat

export CATALINA_HOME=../tomcat
export CATALINA_BASE=.
#export CATALINA_TMPDIR=temp
export JAVA_OPTS=-Xmx500M
#  -Dfile.encoding=8859_1

# export PATH=$PATH:$CATALINA_HOME/bin
# if it's not already defined in your environment, specify the JAVA_HOME directory
# export JAVA_HOME=''

echo $PATH
${CATALINA_HOME}/bin/startup.sh

#tail -f logs/catalina.out