#!/bin/sh

cp -r tomcat/* tomcat-fresh
mkdir -p tomcat-fresh/common/classes/org/makumba/parade/tools
ant compile
cp webapp/WEB-INF/classes/org/makumba/parade/tools/TriggerFilter*.class tomcat-fresh/common/classes/org/makumba/parade/tools/
cp webapp/WEB-INF/classes/org/makumba/parade/tools/HttpServletRequestDummy*.class tomcat-fresh/common/classes/org/makumba/parade/tools/
mkdir  tomcat-fresh/webapps/parade
mkdir  tomcat-fresh/webapps_dummy
cp -r webapp/* tomcat-fresh/webapps/parade
cp lib/mysql.jar lib/ant-1.6.5.jar lib/ant-launcher.jar tomcat-fresh/webapps/parade/WEB-INF/lib/
cp build.* tomcat-fresh/
cp jnotify.dll libjnotify.so tomcat-fresh/
mv tomcat-fresh/ tomcat-parade
