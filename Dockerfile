# 使用 Maven 镜像编译项目
FROM maven:3.8.6-openjdk-17 AS build

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和源代码
COPY pom.xml .
COPY src ./src

# 编译打包
RUN mvn clean package -DskipTests

# 使用 JDK 17 运行
FROM openjdk:17-jdk-slim

WORKDIR /app

# 从编译阶段复制 jar 包
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]