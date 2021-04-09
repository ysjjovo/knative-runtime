#!/bin/bash
set -euxo pipefail
user_code=$1
package_type=$2
tag_or_param=$3
repo=$4
base_tag=$5

if [ x${package_type} == xtest ];then
  docker run --rm -v $user_code:/opt/code $repo:$base_tag /bin/bash init.sh $tag_or_param
else
  docker_tmp_dir=`mktemp -d`
  cd $docker_tmp_dir
  cat >Dockerfile <<EOF
  FROM $repo:$base_tag
  COPY . /opt/code/
EOF
  cp -a $user_code/* $docker_tmp_dir/
  docker build -t $repo:$tag_or_param .
  #clear
  rm -rf $docker_tmp_dir
fi
