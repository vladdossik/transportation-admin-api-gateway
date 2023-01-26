#!/bin/sh

exec java $JAVA_OPTS -Dspring.config.additional-location=file:/etc/config/application.yml -jar /usr/app/app.jar