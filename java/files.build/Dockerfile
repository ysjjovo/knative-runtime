# docker build -t java-maven:1.0.0 .
# docker run --rm -v ~/.m2:/root/.m2 -v ~/app/knative-runtime/java/user-code/:/opt/code/src/main/java/user-code/ java-maven:1.0.0 /bin/bash ./init.sh eyJ0eXBlIjoidGV4dCIsImV4dCI6Imh0bWwifQ==
FROM maven:3.6.3-jdk-11
WORKDIR /opt/code/
COPY . .
CMD ["./init.sh"]
