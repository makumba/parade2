#!/bin/sh

export CATALINA_HOME=/home/projects/parade/util-java/apache-tomcat-5.5.20
export CATALINA_BASE=.

cp ${CATALINA_HOME}/server/lib/catalina-ant.jar ${ANT_HOME}/lib

ant prepareTomcat
