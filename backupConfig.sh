#!/bin/bash

mkdir ../backup_parade_config
cp tomcat.properties ../backup_parade_config
cp webapp/WEB-INF/classes/parade.properties ../backup_parade_config
cp webapp/WEB-INF/classes/rows.properties ../backup_parade_config
cp webapp/WEB-INF/classes/localhost_mysql_parade* ../backup_parade_config