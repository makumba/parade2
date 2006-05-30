#!/bin/sh

cp -r tomcat/* tomcat-fresh/
mkdir -p tomcat-fresh/common/classes/org/makumba/parade/tools
ant compile
cp webapp/WEB-INF/classes/org/makumba/parade/tools/TriggerFilter*.class tomcat-fresh/common/classes/org/makumba/parade/tools/
cp webapp/WEB-INF/classes/org/makumba/parade/tools/HttpServletRequestDummy*.class tomcat-fresh/common/classes/org/makumba/parade/tools/
mkdir -p tomcat-fresh/webapps/parade
mkdir -p tomcat-fresh/webapps_dummy
cp -r webapp/* tomcat-fresh/webapps/parade
cp *.properties tomcat-fresh/
cp lib/mysql.jar tomcat-fresh/webapps/parade/WEB-INF/lib/
mv tomcat-fresh tomcat-parade
