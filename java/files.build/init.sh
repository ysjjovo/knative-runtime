#!/bin/bash
# /bin/bash init.sh eyJ0eXBlIjoidGV4dCIsImV4dCI6Imh0bWwifQ==
set -euxo pipefail
JAVA_OPTS="java -XX:MaxRAMPercentage=75 -cp target/user-code.jar"

# generate pom.xml
deps=""
declare -i i=0
cd $(dirname $0)
for jar in $(ls libs);do
  JAVA_OPTS="$JAVA_OPTS:libs/$jar"
  deps=$deps`eval "cat <<EOF
    $(< dep.xml.template)
EOF"`
  i+=1
done
eval "cat <<EOF
$(< pom.xml.template)
EOF
"  > pom.xml

#build jar
#mvn package
mvn package -Dmaven.test.skip=true

# run or test
if [ $# -eq 0 ];then
 \cp target/user-code.jar  /opt/bin/
else
 $JAVA_OPTS runtime.Test $@
fi