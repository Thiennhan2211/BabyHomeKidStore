# Bước 1: Build code
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Chạy web
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Dùng dấu * để tự động bắt đúng file JAR bất chấp sai lệch chữ hoa/thường
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]