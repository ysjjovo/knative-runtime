java运行时
# 使用说明
- 构建工具为gradle,先用gradle打包出proxy.jar
- proxy.jar需要复制到files.run及files.build里面
- 替换files.run及build里面文件夹为实际的依赖
- 先进入files.build里面运行命令```docker build -t java-maven:1.0.0 .```打包构建及测试环境
- 再进入files.run里面运行命令```docker build -t knative-runtime/java:1.0.0 .```构建函数镜像
- sh目录下有runme通用构建或测试脚本，一键操作代替上面的步骤

