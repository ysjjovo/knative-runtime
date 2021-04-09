#!/bin/bash
set -euxo pipefail
#test_image=openjdk:11
#base_tag=registry.access.redhat.com/ubi8/ubi-minimal:8.3

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
tail="-v $user_code:/project/src/main/java/user-code/ -v $HOME/.m2/repository/:/root/.m2/repository/ $build_image /bin/bash init.sh $package_type"
# build and test
if [ x${package_type} == xtest ];then
  $header $tail $tag_or_param
# build artifact and image
else
  $header -v $artifact_dir/:/opt/bin/ $tail
  cd $artifact_dir
  cat >Dockerfile <<EOF
FROM $repo:$base_tag

WORKDIR /opt/code/
COPY runner .
USER root
RUN chmod 775 runner && chown 1001 runner
EXPOSE 8080
USER 1001
CMD ["./runner"]
EOF
  docker build -t $repo:$tag_or_param .
fi

# clear
rm -rf $artifact_dir