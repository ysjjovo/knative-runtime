使用指引
# 基础镜像
在项目根目录下运行
```shell
docker build -t knative-runtime/nodejs:<baseTag>
```
# 用户函数镜像
在项目根目录的user-code目录下运行
```
docker build -t knative-runtime/nodejs:<tag>
```
如果有html,图片等其它资源也可一同打入镜像

# 函数编写
- 用户需要暴露api的函数需要写在文件```<project_root>/user-code/index.js```中，示例如下：
```nodejs
module.exports = {
  run: function (params) {
          return {
            contentType: `text/html`,
            body: Buffer.from('<html>web contents</html>'))
          };
      },
}
```
- 函数默认入口为main,如果使用其它名称需要设置环境变量
- 函数参数params由请求的body,querystring合并而成。body对应请求体的数据，query对应url传参。
- 函数内容可随意发挥，多文件及其它依赖使用require引入即可。但三方依赖安装在index.js
同级目录的node_modules里面，使用package.json管理依赖是比较好的方式。
- 函数返回值需要是一个json，如果是异步的话使用Promise返回json。
如果返回类型是```application/json```以外的类型，则json须由两个字段组成，
contentType指定类型如静态网页```text/html```，body为Buffer类型，复杂网页可使用
Promise异步读文件。
