#!/usr/bin/env bash

JAVA_OPTS="-server -Xms512m -Xmx512m -Xss1024K  -XX:MaxTenuringThreshold=10"

java $JAVA_OPTS -jar manager.jar 1> dayu.log 2>&1  &
echo  $! > dayu.pid
