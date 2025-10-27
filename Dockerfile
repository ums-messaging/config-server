FROM registry.ums.local:5000/jdk/eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY build/libs/config-server-0.0.1-SNAPSHOT.jar app.jar
COPY /var/jenkins_home/.aws /root/.aws
RUN chmod -R 600 /root/.aws

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
