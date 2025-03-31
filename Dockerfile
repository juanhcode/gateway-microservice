FROM openjdk:23-ea-17-jdk
WORKDIR /app
COPY ./target/gateway-microservice-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway-microservice-0.0.1-SNSNAPSHOT.jar"]