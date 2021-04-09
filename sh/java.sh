#!/bin/bash
set -euxo pipefail
user_code=$1
package_type=$2
tag_or_param=$3
repo=$4
base_tag=$5
build_image=$6

artifact_dir=`mktemp -d`
# build artifact
if [[ `uname  -a` =~ Darwin ]];then
  artifact_dir=/private$artifact_dir
fi

header="docker run --rm"
tail="-v $user_code:/opt/code/src/main/java/user-code/ -v $HOME/.m2/:/root/.m2/ $build_image /bin/bash init.sh"
#test
if [ x${package_type} == xtest ];then
  $header $tail $tag_or_param
#build
else
  $header -v $artifact_dir/:/opt/bin/ $tail
  cd $artifact_dir
  cat >Dockerfile <<EOF
FROM $repo:$base_tag
COPY user-code.jar libs/
EOF
  docker build -t $repo:$tag_or_param .
fi

# clear
rm -rf $artifact_dir