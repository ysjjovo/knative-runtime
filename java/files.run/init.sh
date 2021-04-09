#!/bin/bash
#/bin/bash init.sh
set -euxo pipefail
JAVA_OPTS="java -XX:MaxRAMPercentage=75 -cp "

cd $(dirname $0)
for jar in $(ls libs);do
  JAVA_OPTS="$JAVA_OPTS:libs/$jar"
done
# run
$JAVA_OPTS runtime.Server