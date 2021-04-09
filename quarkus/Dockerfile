# docker build -t quarkus-maven:1.0.0 .
FROM quay.io/quarkus/centos-quarkus-maven:20.2.0-java11
COPY quarkus/ /project
USER root
RUN chown -R quarkus /project
#USER quarkus