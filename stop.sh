#!/bin/sh

export CATALINA_HOME='tomcat'
export CATALINA_BASE='../tomcat'
export JAVA_OPTS='-Xms70M -Xmx500M -Dcatalina.base=tomcat-eclipse -Djava.io.tmpdir="${CATALINA_HOME}/temp" -Dcatalina.home="${CATALINA_HOME}" -Dfile.encoding=8859_1 -Djava.library.path=.'

# if it's not already defined in your environment, specify the JAVA_HOME directory
# export JAVA_HOME=''

${CATALINA_BASE}/bin/shutdown.sh
