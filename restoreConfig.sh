#!/bin/bash

cp ../backup_parade_config/tomcat.properties .
cp ../backup_parade_config/parade.properties webapp/WEB-INF/classes/
cp ../backup_parade_config/rows.properties webapp/WEB-INF/classes/
cp ../backup_parade_config/localhost_mysql_parade* webapp/WEB-INF/classes/
chmod +x parade