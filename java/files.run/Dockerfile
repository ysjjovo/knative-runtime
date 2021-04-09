# docker build -t knative-runtime/java:1.0.0 .
FROM openjdk:11
WORKDIR /opt/code/
COPY . .
EXPOSE 8080
CMD ["/bin/bash", "init.sh"]