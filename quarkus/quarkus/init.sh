#!/bin/bash
# /bin/bash init.sh test eyJ0eXBlIjoidGV4dCIsImV4dCI6Imh0bWwifQ==
set -euxo pipefail
# build artifact
package_type=$1

if [ x$package_type == xtest ];then
  echo 'quarkus.package.main-class=runtime.Test'>src/main/resources/application.properties
  mvn package
  java -XX:MaxRAMPercentage=75 -jar target/quarkus-*-runner.jar $2
else
  mvn package -Pnative
  mv target/*-runner /opt/bin/runner
fi

