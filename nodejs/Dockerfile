# docker build -t knative-runtime-nodejs:1.0.0 .
# docker run --rm -v ~/app/knative-runtime/nodejs/user-code/:/opt/code knative-runtime-nodejs:1.0.0 /bin/bash init.sh eyJ0eXBlIjoidGV4dCIsImV4dCI6Imh0bWwifQ==
FROM node:12.18.2-stretch
ENV LANG C.UTF-8
WORKDIR /app/
COPY index.js .
COPY init.sh .
COPY package.json .
COPY test.js .
COPY sources.list /etc/apt
COPY libvips-8.8.1-linux-x64.tar.gz /root/.npm/_libvips/

RUN apt-get update && apt-get install -y tzdata python make build-essential \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& npm config set registry https://registry.npm.taobao.org \
&& cd / && npm install --no-package-lock \
express@4.16.4 \
body-parser@1.19.0 \
&& npm cache clean --force

CMD ["/bin/bash", "init.sh"]

