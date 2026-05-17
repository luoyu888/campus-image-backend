# 使用 JDK 17 基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 jar 包到容器中
COPY target/*.jar app.jar

# 暴露端口（Spring Boot 默认 8080）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]