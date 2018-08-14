#!/bin/sh

#sleep 2 

nginx -g 'pid /tmp/nginx.pid;'

#nginx -t
#ps aux | grep nginx

java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /app.jar > /mylog/server.log

sleep 999999999


