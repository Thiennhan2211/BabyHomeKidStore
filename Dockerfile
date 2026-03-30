# Bước 1: Mượn một cái máy có sẵn Maven và Java 17 để đóng gói code
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Dùng maven của hệ thống để build luôn, né triệt để lỗi Permission denied
RUN mvn clean package -DskipTests

# Bước 2: Tạo một cái máy ảo Java siêu nhẹ để chạy web
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
# Lấy cục file JAR vừa đóng gói ở Bước 1 mang sang đây
COPY --from=build /app/target/DoubleN_kidstore-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Lệnh khởi động web
ENTRYPOINT ["java", "-jar", "app.jar"]