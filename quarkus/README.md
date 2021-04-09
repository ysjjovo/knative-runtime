quarkus的运行时,和java运行时构建策略不一样，用户函数和quarkus运行时不能分开构建，因此quarkus不使用离线依赖，相对简单点
# 使用说明
- user-code 函数目录，入口类必须为```Main```,不带包名，入口函数为```run```,如果想指定其它类及方法，可使用环境变量MAIN设置
- 入口类及入口函数的输入、输出参数类型上必须加```@RegisterForReflection```注解
- 流函数使用```runtime.model.XObject```作为入口函数返回值类型构建用户函数镜像
- 生成用于构建的基础镜像
在根目录运行
```shell
docker build -t quarkus-maven:1.0.0 .
````
- 测试函数or构建函数镜像
```shell
/bin/bash quarkus.sh <user_code_dir> <package_type> <tag_or_param>
````
- 此项目基于Mac OSX调试成功，linux环境应该修改quarkus.sh里面docker run命令挂载，去掉/private

